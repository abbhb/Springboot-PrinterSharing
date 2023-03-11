package com.qc.printers.service;

import com.qc.printers.common.R;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    R<String> uploadImage(MultipartFile file);
}