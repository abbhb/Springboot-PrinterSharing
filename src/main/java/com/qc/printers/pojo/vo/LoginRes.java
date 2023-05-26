package com.qc.printers.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRes implements Serializable {
    private String token;
}
