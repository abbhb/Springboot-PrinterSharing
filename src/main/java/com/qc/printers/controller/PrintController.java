package com.qc.printers.controller;

import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.service.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.qc.printers.common.MyString.public_file;

@RestController//@ResponseBody+@Controller
@RequestMapping("/printer")
@Slf4j
public class PrintController {

    private final PrintService printService;

    @Autowired
    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @CrossOrigin("*")
    @PostMapping("/uploadpdf")
    //后期可以传回token拿到用户信息
    public R<String> fileupload(MultipartFile file, @PathParam(value = "numberOfPrintedPages") Integer numberOfPrintedPages,@PathParam(value = "printingDirection") Integer printingDirection) {
        log.info("numberOfPrintedPages={},printingDirection={}",numberOfPrintedPages,printingDirection);

        if (file==null){
            return R.error("异常");
        }
        if (numberOfPrintedPages==null){
            return R.error("参数呢");
        }
        if (printingDirection==null){
            return R.error("参数呢");
        }
        //首先要给文件找一个目录
        //先写返回值
        //再用pdf格式开始书写,先找原始的名字
        String originName = file.getOriginalFilename();
        //判断文件类型是不是pdf
        if(!originName.endsWith(".pdf")){
            //如果不是的话，就返回类型
            return R.error("文件类型不对");
        }
        //如果是正确的话，就需要写保存文件的文件夹
        //.format(new Date())的意思是 也就是格式化一个新的时间
        //Date会创建一个时间，然后会按照当前的sdf格式调用format将当前时间创建出来 直接调用new Date()可能会出现这种格式
        //Sun Feb 28 10:55:06 CST 2021
        //再是getServletContext


        //再是保存文件的文件夹
        File folder = new File(public_file);
        //如果不存在，就自己创建
        if(!folder.exists()){
            folder.mkdirs();
        }
        //随机文件名,后期做个记录，跟用户绑定
        String newName = UUID.randomUUID().toString() + ".pdf";

        //然后就可以保存了
        try {
            file.transferTo(new File(folder,newName));
            //存入库加入打印队列
            log.info("路径为:{}",folder+newName);
            //打印
            boolean isPrintSuccess = printService.printsForPDF(newName,originName,numberOfPrintedPages,printingDirection);
            log.info("{}",isPrintSuccess);
            return R.success("打印中,请稍后!");
        } catch (IOException e) {
            //返回异常
            e.printStackTrace();
            throw new CustomException("异常");

        }
    }
}
