package com.qc.printers.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

@Data
public class TrLogin implements Serializable {
    private Long id;

    //第三方平台ID
    private String trId;

    //当前系统的userId
    private Long userId;

    //是否已经完成了注册或者绑定,0未完成,1已经完成
    private Integer status;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDeleted;

    /**
     * 关键参数
     * 标识第三方
     * 1：ENRoom
     * 2:CAS-oauth2.0
     */
    private Integer type;
}
