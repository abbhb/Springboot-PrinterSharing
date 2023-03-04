package com.qc.printers.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.qc.printers.common.CustomException;
import com.qc.printers.service.PrintService;
import com.qc.printers.utils.PdfPrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

import static com.qc.printers.common.MyString.public_file;

@Service
@Slf4j
public class PrintServiceImpl implements PrintService {


    @Override
    public boolean printsForPDF(String newName, String oldName, Integer numberOfPrintedPages,Integer printingDirection) {
        if (numberOfPrintedPages==null){
            throw new CustomException("err");
        }
        if (printingDirection==null){
            throw new CustomException("err");
        }
        try {
            PdfReader pdfReader = new PdfReader(new FileInputStream(public_file+"\\"+newName));

            // 获取总页数
            int pages = pdfReader.getNumberOfPages();
            log.info("{}",pages);
            // 同文件多页 打印 就多次调用 printFile方法
            // i 为页码  当找不到时就停止打印了  ， 这里动态获取页码
            for (int i = 1; i <= pages; i++) {
                PdfPrintUtil.printFile(public_file+"\\"+newName, "Brother HL-2240D series", newName,i, numberOfPrintedPages, printingDirection);
                log.info("publc{},{}",public_file,newName);
            }

            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }
}
