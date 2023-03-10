package com.qc.printers.utils;

import com.qc.printers.common.CustomException;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数校验工具类
 */
public class paramsCalibration {
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
}
