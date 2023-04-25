package com.qc.printers.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.MyString;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.Permission;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.IRedisService;
import com.qc.printers.service.UserService;

/**
 * 参数校验工具类
 */
public class ParamsCalibration {
    public static void checkBeforePrint(Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, Integer numberOfPrintedPagesIndex) {
        if (numberOfPrintedPages == null) {
            throw new CustomException("err: numberOfPrintedPages is null");
        }
        if (printingDirection == null) {
            throw new CustomException("err: printingDirection is null");
        }
        if (printBigValue == null) {
            throw new CustomException("err: printBigValue is null");
        }
        if (numberOfPrintedPagesIndex == null) {
            throw new CustomException("err: numberOfPrintedPagesIndex is null");
        }
    }
    public static void checkSensitiveWords(String word){
        String sensitiveWords = "admin,妈,爸,爹,爷,妈妈,爷爷,爸爸,admins,Admin,ADmin,ADMin,ADMIn,ADMIN,Root,root,ROOT,ROOt,name,<,>";
        if (sensitiveWords.contains(word)){
            throw new CustomException("err: 敏感词");

        }
    }



    /**
     * @param username
     * @return 0:账户密码登录 1:邮箱登录
     */
    public static int booleanLoginType(String username){

        String em = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if (username.matches(em)){
            return 1;
        }else {
            return 0;
        }
    }

    public static boolean checkIsWord(String name){
        if (name.equals("doc")){
            return true;
        } else if (name.equals("docx")) {
            return true;
        }else {
            return false;
        }
    }
    public static boolean checkIsPdf(String name){
        if (name.equals("pdf")){
            return true;
        } else {
            return false;
        }
    }
    public static boolean checkIsCanPrint(String name){
       if (checkIsPdf(name)){
           return true;
       } else if (checkIsWord(name)) {
           return true;
       }else {
           return false;
       }
    }

    /**
     * 双面打印的singleDocumentPaperUsage在此处变更
     * @param oldName
     * @param url
     * @param printingDirection
     * @param copies
     * @param printBigValue
     * @param needPrintPagesEndIndex
     * @param isDuplex
     * @param originFilePages
     * @param singleDocumentPaperUsage
     * @param userId
     * @return
     */
    public static Printer somePrinterParams(String oldName,String url,Integer printingDirection,Integer copies,Integer printBigValue,Integer needPrintPagesEndIndex,Integer isDuplex,Integer originFilePages,Integer singleDocumentPaperUsage ,Long userId){
        Printer printer = new Printer();
        printer.setName(oldName);
        printer.setUrl(url);
        printer.setPrintingDirection(printingDirection);
        printer.setCopies(copies);
        printer.setPrintBigValue(printBigValue);
        printer.setOriginFilePages(originFilePages);
        printer.setNeedPrintPagesEndIndex(needPrintPagesEndIndex);
        printer.setCreateUser(userId);
        printer.setIsDuplex(isDuplex);

        // 双面打印时打印记录中记录使用纸张页数, 向上取整
        if (isDuplex == 1) {
            //设置真实单份文档用纸
            printer.setSingleDocumentPaperUsage(singleDocumentPaperUsage);
            printer.setSingleDocumentPaperUsage((int) Math.ceil((double) singleDocumentPaperUsage / 2));
        }else {
            printer.setSingleDocumentPaperUsage(singleDocumentPaperUsage);
        }
        return printer;
    }
}
