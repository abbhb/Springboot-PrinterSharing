package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.CustomException;
import com.qc.printers.mapper.PrinterMapper;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.service.PrinterService;
import com.qc.printers.utils.FileMD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

import static com.qc.printers.common.MyString.public_file;

@Service
@Slf4j
public class PrinterServiceImpl extends ServiceImpl<PrinterMapper, Printer> implements PrinterService {

    @Transactional
    @Override
    public boolean addPrinter(Printer printer,String urlName) {
        if (StringUtils.isEmpty(printer.getName())){
            log.error("打印记录缺失");
            return false;
        }
        if (printer.getCreateUser()==null){
            log.error("打印记录缺失");
            return false;
        }
        String fileUrl = public_file+"\\"+urlName;
        try {
            printer.setContentHash(FileMD5.md5HashCode(fileUrl));
            boolean save = super.save(printer);
            if (save){
                return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            log.error("打印记录缺失");
            log.error(e.getMessage());
            return false;
        }
    }
}
