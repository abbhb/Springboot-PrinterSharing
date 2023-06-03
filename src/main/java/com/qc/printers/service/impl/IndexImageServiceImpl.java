package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.config.MinIoProperties;
import com.qc.printers.mapper.IndexImageMapper;
import com.qc.printers.pojo.entity.IndexImage;
import com.qc.printers.service.IndexImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IndexImageServiceImpl extends ServiceImpl<IndexImageMapper, IndexImage> implements IndexImageService{

    @Autowired
    MinIoProperties minIoProperties;

    @Override
    public List<String> allLabel() {
        LambdaQueryWrapper<IndexImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(IndexImage::getLabel).groupBy(IndexImage::getLabel);
        List<IndexImage> list = this.list(lambdaQueryWrapper);
        List<String> strings = new ArrayList<>();
        for (IndexImage indexImage : list) {
            strings.add(indexImage.getLabel());
        }
        return strings;
    }

    @Override
    public List<String> labelImage(String label) {
        LambdaQueryWrapper<IndexImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(IndexImage::getImage).eq(IndexImage::getLabel,label);
        List<IndexImage> list = this.list(lambdaQueryWrapper);
        List<String> strings = new ArrayList<>();
        for (IndexImage indexImage : list) {
            strings.add(minIoProperties.getUrl()+"/"+minIoProperties.getBucketName()+"/"+indexImage.getImage());
        }
        return strings;
    }
}
