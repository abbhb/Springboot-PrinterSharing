package com.qc.printers.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 此对象不能直接映射，否则出现问题
 */
@Data
public class Printer implements Serializable {

    /**
     * 份数
     */
    private Integer numberOfPrintedPages;

    /**
     * 打印方向
     * 1:PageFormat.PORTRAIT
     * 0:PageFormat.LANDSCAPE
     */
    private Integer printingDirection;
}
