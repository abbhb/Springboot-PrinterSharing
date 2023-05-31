package com.qc.printers.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordR implements Serializable {
    private String password;

    private String rePassword;

    private String newPassword;
}
