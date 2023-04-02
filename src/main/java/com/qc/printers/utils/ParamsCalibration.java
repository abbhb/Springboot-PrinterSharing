package com.qc.printers.utils;

import com.qc.printers.common.CustomException;
import com.qc.printers.pojo.entity.Printer;

/**
 * 参数校验工具类
 */
public class ParamsCalibration {
    public static void checkBeforePrint(Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex) {
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

    public static Printer somePrinterParams(String oldName,Integer printingDirection,Integer numberOfPrintedPages,Integer printBigValue,String numberOfPrintedPagesIndex,Integer isDuplex ,Long userId){
        Printer printer = new Printer();
        printer.setName(oldName);
        printer.setPrintingDirection(printingDirection);
        printer.setNumberOfPrintedPages(numberOfPrintedPages);
        printer.setPrintBigValue(printBigValue);
        printer.setNumberOfPrintedPagesIndex(numberOfPrintedPagesIndex);
        printer.setCreateUser(userId);
        printer.setIsDuplex(isDuplex);
        return printer;
    }
}
