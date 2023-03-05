package com.qc.printers.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.qc.printers.common.CustomException;
import com.qc.printers.service.PrintService;
import com.qc.printers.utils.PdfPrintUtil;
import com.qc.printers.utils.WordPrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.UUID;

import static com.qc.printers.common.MyString.public_file;

@Service
@Slf4j
public class PrintServiceImpl implements PrintService {


    @Override
    public boolean printsForPDF(String newName, String oldName, Integer numberOfPrintedPages,Integer printingDirection,Integer printBigValue,String numberOfPrintedPagesIndex) {
        if (numberOfPrintedPages==null){
            throw new CustomException("err");
        }
        if (printingDirection==null){
            throw new CustomException("err");
        }
        if (printBigValue==null){
            throw new CustomException("err");
        }
        if (numberOfPrintedPagesIndex==null){
            throw new CustomException("err");
        }
        try {
            PdfReader pdfReader = new PdfReader(new FileInputStream(public_file+"\\"+newName));

            // 获取总页数
            int pages = pdfReader.getNumberOfPages();
//            log.info("{}",pages);
            // 同文件多页 打印 就多次调用 printFile方法
            // i 为页码  当找不到时就停止打印了  ， 这里动态获取页码
            if (numberOfPrintedPagesIndex.equals("all")){
                PdfPrintUtil.printFile(public_file+"\\"+newName, "Brother HL-2240D series", newName,pages, numberOfPrintedPages, printingDirection, printBigValue);
                return true;
            }else {
                PdfPrintUtil.printFile(public_file + "\\" + newName, "Brother HL-2240D series", newName, Integer.parseInt(numberOfPrintedPagesIndex), numberOfPrintedPages, printingDirection, printBigValue);
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean printsForWord(String newName, String originName, Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex) {
        if (numberOfPrintedPages==null){
            throw new CustomException("err");
        }
        if (printingDirection==null){
            throw new CustomException("err");
        }
        if (printBigValue==null){
            throw new CustomException("err");
        }
        if (numberOfPrintedPagesIndex==null){
            throw new CustomException("err");
        }
        try {
            String suffix = StringUtils.substringAfter(newName , ".");//后缀
            String newNamePDF = UUID.randomUUID().toString() + ".pdf";
            WordPrintUtil.wordToPDF(public_file+"\\"+newName,public_file+"\\"+newNamePDF);
            //后面就调用pdf打印就行
            return printsForPDF(newNamePDF,newNamePDF,numberOfPrintedPages,printingDirection,printBigValue,numberOfPrintedPagesIndex);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
