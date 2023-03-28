package com.qc.printers.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

@Data
public class TrLoginEn implements Serializable {
    private Long id;

    //第三方平台ID
    private Long trId;

    //当前系统的userId
    private Long userId;

    //是否已经完成了注册或者绑定,0未完成,1已经完成
    private Integer status;

    @TableLogic
    private Integer isDeleted;
}
