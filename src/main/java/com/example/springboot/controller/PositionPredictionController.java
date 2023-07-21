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
import com.example.springboot.entity.Threaten;
import com.example.springboot.entity.ThreatenAnalyseCopy1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IPositionPredictionService;
import com.example.springboot.entity.PositionPrediction;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-03-29
 */
@RestController
@RequestMapping("/positionPrediction")
public class PositionPredictionController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IPositionPredictionService positionPredictionService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody PositionPrediction positionPrediction) {
        if (positionPrediction.getId() == null) {
            //positionPrediction.setTime(DateUtil.now());
            //positionPrediction.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        positionPredictionService.saveOrUpdate(positionPrediction);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        positionPredictionService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        positionPredictionService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(positionPredictionService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(positionPredictionService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<PositionPrediction> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(positionPredictionService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<PositionPrediction> list = positionPredictionService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("PositionPrediction信息表", "UTF-8");
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
        List<PositionPrediction> list = reader.readAll(PositionPrediction.class);

        //测试阶段删除改表的全部数据
        positionPredictionService.remove(new QueryWrapper<>());

        positionPredictionService.saveBatch(list);
        //ID重复禁止插入,后续更新可以改成覆盖的形式
        //使用idi，主键自增ID无效
//        for(int i=0;i<list.size();i++){
//            if(positionPredictionService.getById(list.get(i).getId()) == null){
//                System.out.println(positionPredictionService.getById(list.get(i).getId()));
//                positionPredictionService.save(list.get(i));
//            }
//        }
        //生成CSV文件，用于python的读取
        //下载服务器数据到本体
        List<PositionPrediction> dataList = positionPredictionService.list();
        //写入本地
        String filePath = fileUploadPath + "tempPositionPredictionData.csv";
        FileWriter fw = null;
        try{
            File tempFile = new File(filePath);
            if (!tempFile.exists()){
                tempFile.createNewFile();
            }
            fw = new FileWriter(filePath);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write("id,timeAtServer,aircraft,latitude,longitude,baroAltitude,geoAltitude,numMeasurements,SerialA,timeAtA,RSSIA,latituteA,longituteA,heightA,SerialB,timeAtB,RSSIB,latitudeB,longituteB,heightB,SerialC,timeAtC,RSSIC,latituteC,longituteC,heightC"+"\n");
            for(int i=0;i<dataList.size();i++){
                bw.write(dataList.get(i).getId()+","
                        +dataList.get(i).getTimeAtServer()+","
                        +dataList.get(i).getAircraft()+","
                        +dataList.get(i).getLatitude()+","
                        +dataList.get(i).getLongitude()+","
                        +dataList.get(i).getBaroAltitude()+","
                        +dataList.get(i).getGeoAltitude()+","
                        +dataList.get(i).getNumMeasurements()+","
                        +dataList.get(i).getSerialA()+","
                        +dataList.get(i).getTimeAtA()+","
                        +dataList.get(i).getRssiA()+","
                        +dataList.get(i).getLatituteA()+","
                        +dataList.get(i).getLongituteA()+","
                        +dataList.get(i).getHeightA()+","
                        +dataList.get(i).getSerialB()+","
                        +dataList.get(i).getTimeAtB()+","
                        +dataList.get(i).getRssiB()+","
                        +dataList.get(i).getLatitudeB()+","
                        +dataList.get(i).getLongituteB()+","
                        +dataList.get(i).getHeightB()+","
                        +dataList.get(i).getSerialC()+","
                        +dataList.get(i).getTimeAtC()+","
                        +dataList.get(i).getRssiC()+","
                        +dataList.get(i).getLatituteC()+","
                        +dataList.get(i).getLongituteC()+","
                        +dataList.get(i).getHeightC()
                );
                bw.write("\n");
            }
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

        //删除临时文件
        try {
            Files.deleteIfExists(
                    Paths.get(fileUploadPath + "tempPositionPredictionData.json"));
        }
        catch (NoSuchFileException e) {
            System.out.println(
                    "No such file/directory exists");
        }
        catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        }
        catch (IOException e) {
            System.out.println("Invalid permissions.");
        }
        System.out.println("Deletion successful.");

        return Result.success();
    }



    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

