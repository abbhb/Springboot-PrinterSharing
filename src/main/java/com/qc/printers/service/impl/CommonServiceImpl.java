package com.qc.printers.service.impl;

import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.config.MinIoProperties;
import com.qc.printers.service.CommonService;
import com.qc.printers.utils.MinIoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    MinIoProperties minIoProperties;
    @Override
    public R<String> uploadImage(MultipartFile file) {
        try {
            String fileUrl = MinIoUtil.upload(minIoProperties.getBucketName(), file);
            log.info("imageUrl={}",fileUrl);
            String[] split = fileUrl.split("\\?");

            return R.successOnlyObject(split[0]);
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }
}