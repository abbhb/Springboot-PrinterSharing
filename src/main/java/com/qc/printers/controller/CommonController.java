package com.qc.printers.controller;

import com.qc.printers.common.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        return commonService.uploadImage(file);

    }
}
