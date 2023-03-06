package com.qc.printers.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageData<T> implements Serializable {
    private String countId;

    private Long current;

    private Long pages;

    private List<T> records;

    private Long size;

    private Long MaxLimit;

    private Long total;
}