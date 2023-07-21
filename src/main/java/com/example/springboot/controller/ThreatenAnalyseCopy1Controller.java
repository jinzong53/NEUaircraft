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
import com.example.springboot.utils.AnacondaStart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IThreatenAnalyseCopy1Service;
import com.example.springboot.entity.ThreatenAnalyseCopy1;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/threatenAnalyseCopy1")
public class ThreatenAnalyseCopy1Controller {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IThreatenAnalyseCopy1Service threatenAnalyseCopy1Service;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody ThreatenAnalyseCopy1 threatenAnalyseCopy1) {
        if (threatenAnalyseCopy1.getId() == null) {
            //threatenAnalyseCopy1.setTime(DateUtil.now());
            //threatenAnalyseCopy1.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        threatenAnalyseCopy1Service.saveOrUpdate(threatenAnalyseCopy1);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        threatenAnalyseCopy1Service.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        threatenAnalyseCopy1Service.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(threatenAnalyseCopy1Service.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(threatenAnalyseCopy1Service.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ThreatenAnalyseCopy1> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(threatenAnalyseCopy1Service.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<ThreatenAnalyseCopy1> list = threatenAnalyseCopy1Service.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("ThreatenAnalyseCopy1信息表", "UTF-8");
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
        List<ThreatenAnalyseCopy1> list = reader.readAll(ThreatenAnalyseCopy1.class);
        threatenAnalyseCopy1Service.saveBatch(list);
        return Result.success();
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
     * 判断威胁处理文件是否存在
     * @param tDataID
     * @return
     */
    @GetMapping("/isExist")
    public Result isExist(@RequestParam String tDataID){
        tDataID = tDataID + "tempThreatenData.json";
        File file = new File(fileUploadPath + tDataID);
        if(file.exists()){
            return Result.success(1);
        }else{
            return Result.success(0);
        }
    }

    /**
     * 先启动py文件，再将json文件存入服务器，原理同上方保持一直
     * @param tDataID
     * @param startValue
     * @return
     */
    @GetMapping("/addTJson")
    public Result addTJson(@RequestParam String tDataID,@RequestParam String startValue) throws IOException, InterruptedException {
        //生成python预测需要的参数文件
        String filePath = fileUploadPath + "tempTreat.txt";
        FileWriter fw = null;
        try{
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(filePath);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(startValue+"\n"); //gray或者TOPSS，如果是其他情况，由python来出来
            bw.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                fw.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        //启动python的treat.py
        String evn = AnacondaStart.PYTHONEVN;
        String pyt = AnacondaStart.TREAT;

        System.out.println(AnacondaStart.PYTHONEVN);

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

        //清空数据库
        threatenAnalyseCopy1Service.remove(new QueryWrapper<>());

        //读取JSON
        tDataID = tDataID + "tempThreatenData.json";
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
            List<ThreatenAnalyseCopy1> tList = new ArrayList<>();
            //挨个遍历item
            for (Object item: data) {
                JSONObject itemObject =(JSONObject)item;
                JSONObject dataObject = itemObject.getJSONObject("data");
                ThreatenAnalyseCopy1 threatenAnalyseCopy1 = new ThreatenAnalyseCopy1();
                String id = dataObject.getString("id");threatenAnalyseCopy1.setId(Integer.parseInt(id));
                String name = dataObject.getString("name");threatenAnalyseCopy1.setName(name);
                String height = dataObject.getString("treat_height");threatenAnalyseCopy1.setHeight(height);
                String type = dataObject.getString("treat_mod");threatenAnalyseCopy1.setType(type);
                String velocity = dataObject.getString("treat_speed");threatenAnalyseCopy1.setVelocity(velocity);
                String angle = dataObject.getString("treat_angle");threatenAnalyseCopy1.setAngle(angle);
                String distance = dataObject.getString("treat_distance");threatenAnalyseCopy1.setDistance(distance);
                String totalScore = dataObject.getString("total_score");threatenAnalyseCopy1.setTotalScore(totalScore);
                String grayScore = dataObject.getString("gray_score");threatenAnalyseCopy1.setGrayScore(grayScore);
                String topsisScore = dataObject.getString("topsis_score");threatenAnalyseCopy1.setTopsisScore(topsisScore);
                tList.add(threatenAnalyseCopy1);
                if(threatenAnalyseCopy1Service.getById(id) == null){
                    threatenAnalyseCopy1Service.save(threatenAnalyseCopy1);
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

