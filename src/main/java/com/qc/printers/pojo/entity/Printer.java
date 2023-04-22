package com.qc.printers.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 此对象不能直接映射，否则出现问题
 */
@Data
public class Printer implements Serializable {

    private Long id;

    /**
     * 份数
     * todo 建议变量名改为copies,
     */
    private Integer numberOfPrintedPages;

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
     * 需要解析那些页码，或者all
     * todo 类型改为integer, -1代表all, 其他代表页码截至号, 变量名改为endPage
     */
    private String numberOfPrintedPagesIndex;

    private String contentHash;

    private String name;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 谁打印
     */
    private Long createUser;

    /**
     * 单双面
     * false单面 true 双面
     */
    private Integer isDuplex;

    //文件地址
    private String url;
    
    
}