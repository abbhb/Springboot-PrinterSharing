package com.qc.printers.service;

import com.qc.printers.common.R;
import com.qc.printers.pojo.entity.ToEmail;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    R<String> uploadFileTOMinio(MultipartFile file);

    String getImageUrl(String imageKey);

    R<String> sendEmailCode(ToEmail toEmail);

    Integer countApi();

    Integer apiCountLastday();
}