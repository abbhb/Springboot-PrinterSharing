package com.qc.printers.pojo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexImage implements Serializable {
    private Long id;

    private String label;

    private String image;
}
