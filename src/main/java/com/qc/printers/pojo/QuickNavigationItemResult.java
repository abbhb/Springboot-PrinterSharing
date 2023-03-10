package com.qc.printers.pojo;

import lombok.Data;

import java.util.List;

@Data
public class QuickNavigationItemResult {
    private String id;
    private String name;
    private String path;
    private List<Integer> permission;
    private String image;
    private String introduction;

    private String categorizeId;

    private String categorizeName;
}
