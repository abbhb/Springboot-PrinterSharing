package com.qc.printers.common.annotation;


import io.swagger.annotations.ApiOperation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 若直接使用该注解将失效，必须搭配NeedToken使用
 */
@ApiOperation("若直接使用该注解将失效，必须搭配NeedToken使用")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCheck {
    String value() default "2";//默认所有用户都有权限
}
