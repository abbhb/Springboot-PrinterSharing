package com.qc.printers.common;

import com.qc.printers.pojo.UserResult;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用结果类
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code;//编码：1成功，0和其它数字为失败,900删除前端token

    private String msg;//错误信息

    private T data;//数据

    private Map map = new HashMap();//动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> successOnlyObject(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }
    public static <T> R<T> success(String msg) {
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = 1;
        return r;
    }



    public static <T> R<T> successOnlyMsg(String msg,Integer code) {
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = code;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public static <T> R<T> error(Integer code,String msg) {
        R r = new R();
        r.msg = msg;
        r.code = code;
        return r;
    }

    public static <T> R<T> successOnlyObjectWithStatus(T object,Integer code) {
        R<T> r = new R<T>();
        r.code = code;
        r.data = object;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}