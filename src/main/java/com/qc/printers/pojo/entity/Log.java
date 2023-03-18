package com.qc.printers.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "log")
public class Log {
    /**
     * 权限角色ID
     */
    private String id;

    private String userId; //用户Id

    private String method; //方法名

    private String params; //参数

    private String ip; //ip地址

    private String url; //请求url

    private String type; //操作类型 :新增、删除等等

    private String model; //模块

    private LocalDateTime createTime; //操作时间

    private String result; //操作结果

    private String description;//描述

}