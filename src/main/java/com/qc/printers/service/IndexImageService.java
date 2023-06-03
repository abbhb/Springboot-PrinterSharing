package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.pojo.entity.IndexImage;

import java.util.List;


public interface IndexImageService extends IService<IndexImage> {
    List<String> allLabel();


    List<String> labelImage(String label);
}
