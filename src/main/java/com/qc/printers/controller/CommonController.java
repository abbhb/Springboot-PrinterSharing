package com.qc.printers.controller;

import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.common.annotation.PermissionCheck;
import com.qc.printers.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/common")
@Api("公共接口")
public class CommonController {
    @Autowired
    private CommonService commonService;
    @CrossOrigin("*")
    @NeedToken
    @PostMapping("/uploadimage")
    @ApiOperation("上传图像到minio上，返回url,启用了跨域")
    public R<String> uploadImage(MultipartFile file){
        return commonService.uploadFileTOMinio(file);

    }

    //此接口后期需要优化，存日志时顺便写一下redis，然后从redis中取
    @CrossOrigin("*")
    @GetMapping("/api_count")
    @ApiOperation(value = "日总请求数",notes = "")
    public R<Integer> userCount(){
        log.info("获取日总请求数");
        Integer integer = commonService.countApi();
        return R.success(integer);
    }

    @CrossOrigin("*")
    @GetMapping("/api_count_lastday")
    @ApiOperation(value = "昨日请求数",notes = "")
    public R<Integer> userCountToday(){
        log.info("获取昨日请求数");
        Integer integer = commonService.apiCountLastday();
        if (integer == null){
            return R.success(0);
        }
        return R.success(integer);
    }
}
