package com.qc.printers.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.common.annotation.PermissionCheck;
import com.qc.printers.pojo.PrinterResult;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.ValueLabelResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.service.PrintService;
import com.qc.printers.service.PrinterService;
import com.qc.printers.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.qc.printers.common.MyString.public_file;

@RestController//@ResponseBody+@Controller
@RequestMapping("/printer")
@Slf4j
@Api("共享打印相关api")
public class PrintController {

    private final PrintService printService;

    private final PrinterService printerService;

    @Autowired
    public PrintController(PrintService printService, PrinterService printerService) {
        this.printService = printService;
        this.printerService = printerService;
    }

    @CrossOrigin("*")
    @PostMapping("/uploadPrint")
    @NeedToken
    @ApiOperation(value = "打印通用接口")
    //后期可以传回token拿到用户信息
    public R<String> uploadPrint(MultipartFile file, @PathParam(value = "numberOfPrintedPages") Integer numberOfPrintedPages,@PathParam(value = "printingDirection") Integer printingDirection,@PathParam(value = "printBigValue") Integer printBigValue,@PathParam(value = "numberOfPrintedPagesIndex") String numberOfPrintedPagesIndex,@PathParam(value = "isDUPLEX") Integer isDuplex,@RequestHeader(value="Authorization", defaultValue = "") String token) {
        log.info("numberOfPrintedPages={},printingDirection={},numberOfPrintedPagesIndex={},printBigValuw={},isDUPLEX={}",numberOfPrintedPages,printingDirection,numberOfPrintedPagesIndex,printBigValue,isDuplex);

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
        String suffix = StringUtils.substringAfterLast(originName , ".");
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
                boolean isPrintSuccess = printService.printsForPDF(newName,originName,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex,isDuplex,userId);
//                log.info("{}",isPrintSuccess);
                if (isPrintSuccess){
                    return R.success("打印成功,请稍后!");
                }
                return R.error("打印失败");
            } else if (suffix.equals("docx")) {
                boolean isPrintSuccess = printService.printsForWord(newName,originName,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex,isDuplex,userId);
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


    /**
     * 获取历史打印记录
     * 需要分页
     * 将管理员接口和用户接口分离 方便接入权限过滤器
     * 此处没必要校验token 后期通过权限注解标注1需要管理员权限即可
     * @param name 模糊查询 根据文件名筛选(user：id不为空就得带上user 的id)
     * @param user 传回user的id只有管理员可以
     * @param date 传回日期范围筛选 为后期优化预留
     * @param pageNum 分页之当前页
     * @param pageSize 分页之页面最大
     * @return
     */
    @GetMapping("/getAllHistoryPrints")
    @NeedToken
    @PermissionCheck("1")
    @ApiOperation(value = "获取历史打印记录",notes = "传回type参数0为自己的，1为所有人历史记录：需要有管理员权限")
    public R<PageData<PrinterResult>> getAllHistoryPrints(Integer pageNum, Integer pageSize, String name, String date , String user){
        if (pageNum==null){
            return R.error("传参错误");
        }
        if (pageSize==null){
            return R.error("传参错误");
        }
        return printerService.listAllPrinter(pageNum,pageSize,name,user);
    }
    @GetMapping("/getAllUserPrinter")
    @NeedToken
    @PermissionCheck("1")
    @ApiOperation(value = "获取所有打印者",notes = "没打印过的也会包括在内")
    public R<List<ValueLabelResult>> getAllUserPrinter(){
        return printerService.getAllUserPrinter();
    }

    @GetMapping("/getUserPrintTopList")
    @NeedToken
    @PermissionCheck("1")
    @ApiOperation(value = "获取打印榜前10名用户",notes = "会排序好返回")
    public R<List<UserResult>> getUserPrintTopList(){
        return printerService.getUserPrintTopList();
    }
    /**
     * 获取历史打印记录
     * 需要分页
     * 将管理员接口和用户接口分离 方便接入权限过滤器
     * @param name 模糊查询 根据文件名筛选(user：id不为空就得带上user 的id)
     * @param date 传回日期范围筛选 为后期优化预留
     * @param pageNum 分页之当前页
     * @param pageSize 分页之页面最大
     * @return
     */
    @GetMapping("/getMyHistoryPrints")
    @ApiOperation(value = "获取本人历史打印记录",notes = "因为没有token过不了needtoken，所以没必要再次校验token")
    @NeedToken
    public R<PageData<PrinterResult>> getMyHistoryPrints(@RequestHeader(value="Authorization", defaultValue = "") String token,Integer pageNum, Integer pageSize, String name, String date){
        if (pageNum==null){
            return R.error("传参错误");
        }
        if (pageSize==null){
            return R.error("传参错误");
        }

        return printerService.listPrinter(pageNum,pageSize,token,name,date);
    }
}
