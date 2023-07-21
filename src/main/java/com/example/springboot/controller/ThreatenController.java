package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IThreatenService;
import com.example.springboot.entity.Threaten;

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
@RequestMapping("/threaten")
public class ThreatenController {

    @Resource
    private IThreatenService threatenService;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Threaten threaten) {
        if (threaten.getId() == null) {
            //threaten.setTime(DateUtil.now());
            //threaten.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        threatenService.saveOrUpdate(threaten);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        threatenService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        threatenService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(threatenService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(threatenService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Threaten> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(threatenService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Threaten> list = threatenService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Threaten信息表", "UTF-8");
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
        List<Threaten> list = reader.readAll(Threaten.class);

        //清空数据库
        threatenService.remove(new QueryWrapper<>());

        for(int i=0;i<list.size();i++){
            if(threatenService.getById(list.get(i).getId()) == null){
                threatenService.save(list.get(i));
            }
        }
        //threatenService.saveBatch(list);这种方法会报错，因为会有重复id
        //按照CSV的方式保存文件到files目录，等待python来读取文件，然后python将结果OS到files中
        //下载服务器数据到本体
        List<Threaten> dataList = threatenService.list();
        //写入本地
        String filePath = fileUploadPath + "tempThreatenData.csv";
        FileWriter fw = null;
        try{
            File tempFile = new File(filePath);
            if (!tempFile.exists()){
                tempFile.createNewFile();
            }
            fw = new FileWriter(filePath);
            BufferedWriter bw=new BufferedWriter(fw);
            for(int i=0;i<dataList.size();i++){
                bw.write(dataList.get(i).getType()+","
                        +dataList.get(i).getDistance()+","
                        +dataList.get(i).getHeight()+","
                        +dataList.get(i).getAngle()+","
                        +dataList.get(i).getVelocity()+","
                        +dataList.get(i).getId()+","
                        +dataList.get(i).getName()
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
                    Paths.get(fileUploadPath + "tempThreatenData.json"));
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

