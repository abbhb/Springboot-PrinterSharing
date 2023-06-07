package com.qc.printers.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class PrinterResult implements Serializable {
    private String id;

    /**
     * 份数
     */
    private Integer copies;

    /**
     * 打印方向
     * 1:PageFormat.PORTRAIT
     * 0:PageFormat.LANDSCAPE
     */
    private Integer printingDirection;

    /**
     * 大小
     实际大小 ACTUAL_SIZE,
     *缩小     SHRINK_TO_FIT,
     *拉伸     STRETCH_TO_FIT,
     *适应     SCALE_TO_FIT;
     */
    private Integer printBigValue;

    /**
     * 单份文件用纸数
     */
    private Integer singleDocumentPaperUsage;
    /**
     * 【单份】打印页数
     */
    private Integer needPrintPagesEndIndex;

    private Integer needPrintPagesIndex;

    private Integer originFilePages;

    private String contentHash;

    private String name;

    private LocalDateTime createTime;
    /**
     * 谁打印
     */
    private String createUser;

    private Integer isDuplex;

    private String url;



}
