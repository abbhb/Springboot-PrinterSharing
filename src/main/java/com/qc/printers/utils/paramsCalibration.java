package com.qc.printers.utils;

import com.qc.printers.common.CustomException;

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
}
