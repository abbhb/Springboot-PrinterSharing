package com.qc.printers.common;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class CustomException extends RuntimeException{
    private Integer code;
    public CustomException(String msg){
        super(msg);
    }
    public CustomException(String msg,Integer code){
        super(msg);
        this.code = code;
    }
}
