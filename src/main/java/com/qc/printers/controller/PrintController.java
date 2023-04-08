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
import com.qc.printers.pojo.vo.CountTop10VO;
import com.qc.printers.service.CommonService;
import com.qc.printers.service.PrintService;
import com.qc.printers.service.PrinterService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.WordPrintUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    private final CommonService commonService;

    private final PrinterService printerService;

    @Autowired
    public PrintController(PrintService printService, CommonService commonService, PrinterService printerService) {
        this.printService = printService;
        this.commonService = commonService;
        this.printerService = printerService;
    }

    @CrossOrigin("*")
    @PostMapping("/uploadPrint")
    @NeedToken
    @ApiOperation(value = "打印通用接口")
    //后期可以传回token拿到用户信息
    public R<String> uploadPrint(MultipartFile file, @PathParam(value = "numberOfPrintedPages") Integer numberOfPrintedPages,@PathParam(value = "printingDirection") Integer printingDirection,@PathParam(value = "printBigValue") Integer printBigValue,@PathParam(value = "numberOfPrintedPagesIndex") String numberOfPrintedPagesIndex,@PathParam(value = "isDUPLEX") Integer isDuplex,@RequestHeader(value="Authorization", defaultValue = "") String token) {
        log.info("numberOfPrintedPages={},printingDirection={},numberOfPrintedPagesIndex={},printBigValuw={},isDUPLEX={}",numberOfPrintedPages,printingDirection,numberOfPrintedPagesIndex,printBigValue,isDuplex);
        if (file==null||numberOfPrintedPages==null||printingDirection==null){
            return R.error("参数异常");
        }
        if (printBigValue==null){
            printBigValue = 3;//给默认
        }
        if (StringUtils.isEmpty(numberOfPrintedPagesIndex)){
            numberOfPrintedPagesIndex = "all";//给默认,默认全部打印
        }
        //再用pdf格式开始书写,先找原始的名字
        String originName = file.getOriginalFilename();
        if (originName.contains("\\?")||originName.contains("？")){
            return R.error("文件名里不允许包含？请修改后在打印");
        }

        String suffix = StringUtils.substringAfterLast(originName , ".");
        //判断文件类型是不是pdf
        if((!suffix.equals("pdf"))&&(!suffix.equals("docx"))){
            //如果不是的话，就返回类型
            return R.error("文件类型不对");
        }
        //先在minio上传一份原始文件,若打印失败可以调用其余方式
        String fileURL = commonService.uploadFileTOMinio(file).getData();
        if (suffix.equals("pdf")){
            //文件到minio上
            if (StringUtils.isEmpty(fileURL)){
                throw new CustomException("打印失败:commonService.uploadFileTOMinio(file);");
            }
            boolean isPrintSuccess = printService.printsForPDF(fileURL,originName,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex,isDuplex,JWTUtil.getUserId(token));
            if (isPrintSuccess){
                return R.success("打印成功,请稍后!");
            }
            return R.error("打印失败");
        } else if (suffix.equals("docx")) {
            //保存到本地
            String newFileName = WordPrintUtil.saveComputer(file);//本地文件均为缓存,可以手动删除
            boolean isPrintSuccess = printService.printsForWord(newFileName,fileURL,originName,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex,isDuplex,JWTUtil.getUserId(token));
            if (isPrintSuccess){
                return R.success("打印成功,请稍后!");
            }
            return R.error("打印失败");
        }
        return R.error("打印失败");
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
    @ApiOperation(value = "获取打印榜前10名用户",notes = "会排序好返回")
    public R<List<CountTop10VO>> getUserPrintTopList(@ApiParam("type:1为总，2为每天") Integer type){
        return printerService.getUserPrintTopList(type);
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
