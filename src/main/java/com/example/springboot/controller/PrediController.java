package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Predict;
import com.example.springboot.utils.AnacondaStart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IPrediService;
import com.example.springboot.entity.Predi;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-04-24
 */
@RestController
@RequestMapping("/predi")
public class PrediController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IPrediService prediService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Predi predi) {
        if (predi.getId() == null) {
            //predi.setTime(DateUtil.now());
            //predi.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        prediService.saveOrUpdate(predi);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        prediService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        prediService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(prediService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(prediService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Predi> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(prediService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Predi> list = prediService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Predi信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Predi> list = reader.readAll(Predi.class);

        prediService.saveBatch(list);
        return Result.success();
    }
    /**
     * 判断威胁处理文件是否存在
     * @param tDataID
     * @return
     */
    @GetMapping("/isExist")
    public Result isExist(@RequestParam String tDataID){
        tDataID = tDataID + "tempPositionPredictionData.json";
        File file = new File(fileUploadPath + tDataID);
        if(file.exists()){
            return Result.success(1);
        }else{
            return Result.success(0);
        }
    }

    /**
     * 工具函数，勿动
     * @return
     */
    public static String getJsonStr(File jsonFile){
        String jsonStr = "";
        try {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 先启动py文件，再将json文件存入服务器，原理同上方保持一直
     * @param tDataID
     * @return
     */
    @GetMapping("/addTJson")
    public Result addTJson(@RequestParam String tDataID) throws IOException, InterruptedException {
        //启动python的treat.py
        String evn = AnacondaStart.PYTHONEVN;
        String pyt = AnacondaStart.PREDICT;
        Process process;
        process = Runtime.getRuntime().exec(evn+" " +
                pyt);
        //System.out.println(process.waitFor());
        int re=process.waitFor();//re=0成功，re=1失败
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        in = new BufferedReader(new InputStreamReader(process.getInputStream(),"gbk"));
        //接收错误流
        BufferedReader isError = new BufferedReader(new InputStreamReader(process.getErrorStream(),"gbk"));
        StringBuilder sb= new StringBuilder();
        StringBuilder sbError= new StringBuilder();
        String line=null;
        String lineError= null;
        process.waitFor();
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        System.out.println(sb);

        while ((lineError= isError.readLine()) != null) {
            sbError.append(lineError);
            sbError.append("\n");
        }
        System.out.println(sbError);
        in.close();
        isError.close();

        //测试阶段删除改表的全部数据
        prediService.remove(new QueryWrapper<>());

        tDataID = tDataID + "tempPositionPredictionData.json";
        File file = new File(fileUploadPath + tDataID);
        if(file.exists()){
            //获取对象列表
            String json = fileUploadPath + tDataID;
            File jsonFile = new File(json);
            //通过上面那个方法获取json文件的内容
            String jsonData = getJsonStr(jsonFile);
            //转json对象
            JSONObject parse = (JSONObject)JSONObject.parse(jsonData);
            //获取主要数据
            JSONArray data = parse.getJSONArray("data");
            //临时存储数据，用于上传数据库
            List<Predi> tList = new ArrayList<>();
            //挨个遍历item
            for (Object item: data) {
                JSONObject  itemObject =(JSONObject)item;
                Predi predi = new Predi();
                String id = itemObject.getString("id");predi.setId(Integer.parseInt(id));
                String name = itemObject.getString("aircraft");predi.setName(name);
                String timeAtServer = itemObject.getString("timeAtServer");predi.setTime(timeAtServer);
                String pred_latitude = itemObject.getString("pred_latitude");predi.setPredla(pred_latitude);
                String pred_longitude = itemObject.getString("pred_longitude");predi.setPredlo(pred_longitude);
                String pred_geoAltitude = itemObject.getString("pred_geoAltitude");predi.setPreda(pred_geoAltitude);
                tList.add(predi);
                //因为飞机的ID是唯一的
                if(prediService.getById(id) == null){
                    prediService.save(predi);
                }
            }
            return Result.success(1);
        }else{
            return Result.success(0);
        }
    }
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

