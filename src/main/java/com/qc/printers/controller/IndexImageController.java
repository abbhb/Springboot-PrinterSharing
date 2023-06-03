package com.qc.printers.controller;

import com.qc.printers.common.R;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.pojo.entity.IndexImage;
import com.qc.printers.service.IndexImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController//@ResponseBody+@Controller
@RequestMapping("/index_image")
@Slf4j
@CrossOrigin("*")
@Api("共享打印相关api")
public class IndexImageController {
    private final IndexImageService indexImageService;

    public IndexImageController(IndexImageService indexImageService) {
        this.indexImageService = indexImageService;
    }

    @GetMapping("/all_label")
    @NeedToken
    @ApiOperation(value = "获取所有的不同标签",notes = "")
    public R<List<String>> allLabel(){
        log.info("获取所有的不同标签");
        return R.success(indexImageService.allLabel());
    }
    @GetMapping("/label_all")
    @NeedToken
    @ApiOperation(value = "获取所有该标签的image",notes = "")
    public R<List<String>> labelImage(String label){
        log.info("获取所有的不同标签");
        return R.success(indexImageService.labelImage(label));
    }
}
