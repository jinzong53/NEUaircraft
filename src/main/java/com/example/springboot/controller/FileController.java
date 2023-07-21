package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.FileMapper;
import com.example.springboot.utils.AnacondaStart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;
import org.python.core.*;
import org.python.util.*;
/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    /**
     * 显示接口
     * 调用Python文件 返回图片地址
     * @return
     */
    @GetMapping("/print")
    public PyObject HelloPython() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("C:\\D\\airplane\\springboot\\predict4.py");

        PyFunction pyFunction = interpreter.get("detect4.py", PyFunction.class); // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyObject pyObject = pyFunction.__call__(); // 调用函数2

        System.out.println(pyObject);
        return pyObject;
    }
    /**
     * 文件上传接口
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            return "fail"; //返回服务器的文件名
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUUID;
        }


        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return fileUUID; //返回服务器的文件名
    }

    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        try {
            os.write(FileUtil.readBytes(uploadFile));
        } catch (Exception e) {
            System.err.println("文件下载失败，文件不存在");
        }
        os.flush();
        os.close();

        //生成资料文件

    }


    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 建立本地文件，并检测数据
     * @param fileState
     * @param fileModel
     * @param fileName
     * @param fileWeight
     * @return
     */
    @GetMapping("/setPicData")
    public Result setPicData(@RequestParam String fileState,
                           @RequestParam String fileModel,
                           @RequestParam String fileName,
                           @RequestParam String fileWeight) throws IOException, InterruptedException {

        String filePath = fileUploadPath + "tempData.txt";
        FileWriter fw = null;
        try{
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(filePath);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(fileState+"\n");
            bw.write(fileModel+"\n");
            bw.write(fileName+"\n");
            bw.write(fileWeight+"\n");
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


        //启动python文件
        //System.out.println("test");
        String evn = AnacondaStart.PYTHONEVN;
        String pyt =  AnacondaStart.DETECT;
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


        return Result.success(fileName);
    }

    /**
     * 使用图片ID判定检测完成的图片是否存在
     * @param picID
     * @return
     */
    @GetMapping("/isExist")
    public Result isExist(@RequestParam String picID){
        File file = new File(fileUploadPath + "detect_"+ picID);
        if(file.exists()){
            return Result.success(1);
        }else{
            return Result.success(0);

        }
    }
}
