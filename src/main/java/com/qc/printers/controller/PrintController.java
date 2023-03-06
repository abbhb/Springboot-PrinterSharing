package com.qc.printers.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.service.PrintService;
import com.qc.printers.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public R<String> fileupload(MultipartFile file, @PathParam(value = "numberOfPrintedPages") Integer numberOfPrintedPages,@PathParam(value = "printingDirection") Integer printingDirection,@PathParam(value = "printBigValue") Integer printBigValue,@PathParam(value = "numberOfPrintedPagesIndex") String numberOfPrintedPagesIndex,@RequestHeader(value="Authorization", defaultValue = "") String token) {
        log.info("numberOfPrintedPages={},printingDirection={},numberOfPrintedPagesIndex={},printBigValuw={}",numberOfPrintedPages,printingDirection,numberOfPrintedPagesIndex,printBigValue);

        if (file==null){
            return R.error("异常");
        }
        if (numberOfPrintedPages==null){
            return R.error("参数呢");
        }
        if (printingDirection==null){
            return R.error("参数呢");
        }
        if (printBigValue==null){
            printBigValue = 3;//给默认
        }
        if (StringUtils.isEmpty(numberOfPrintedPagesIndex)){
            numberOfPrintedPagesIndex = "all";//给默认,默认全部打印
        }
        Long userId = 0L;
        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim id = decodedJWT.getClaim("id");
            if (StringUtils.isEmpty(id.asString())){
                userId = 0L;
            }else {
                userId = Long.valueOf(id.asString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //首先要给文件找一个目录
        //先写返回值
        //再用pdf格式开始书写,先找原始的名字
        String originName = file.getOriginalFilename();
        String suffix = StringUtils.substringAfter(originName , ".");
        //判断文件类型是不是pdf
        if((!suffix.equals("pdf"))&&(!suffix.equals("docx"))){
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
        String newName = UUID.randomUUID().toString() + "."+suffix;

        //然后就可以保存了
        try {
            file.transferTo(new File(folder,newName));
            //存入库加入打印队列
            log.info("路径为:{}",folder+newName);
            //打印
            if (suffix.equals("pdf")){
                boolean isPrintSuccess = printService.printsForPDF(newName,originName,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex,userId);
//                log.info("{}",isPrintSuccess);
                if (isPrintSuccess){
                    return R.success("打印成功,请稍后!");
                }
                return R.error("打印失败");
            } else if (suffix.equals("docx")) {
                boolean isPrintSuccess = printService.printsForWord(newName,originName,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex,userId);
                if (isPrintSuccess){
                    return R.success("打印成功,请稍后!");
                }
                return R.error("打印失败");
            }
            return R.error("打印失败");

        } catch (IOException e) {
            //返回异常
            e.printStackTrace();
            throw new CustomException("异常");

        }
    }
}
