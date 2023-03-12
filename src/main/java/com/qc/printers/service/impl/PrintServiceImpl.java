package com.qc.printers.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.qc.printers.common.CustomException;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.service.PrintService;
import com.qc.printers.service.PrinterService;
import com.qc.printers.utils.PdfPrintUtil;
import com.qc.printers.utils.WordPrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.qc.printers.common.MyString.public_file;
import static com.qc.printers.utils.paramsCalibration.checkBeforePrint;

@Service
@Slf4j
public class PrintServiceImpl implements PrintService {
    
    private final PrinterService printerService;
    
    @Autowired
    public PrintServiceImpl(PrinterService printerService) {
        this.printerService = printerService;
    }
    
    @Transactional
    @Override
    public boolean printsForPDF(String newName, String oldName, Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex, Long userId) {
        checkBeforePrint(numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex);
        if (numberOfPrintedPagesIndex.contains("-")){
            String[] split = numberOfPrintedPagesIndex.split("-");
            numberOfPrintedPagesIndex = split[1];
        }
        try {
            PdfReader pdfReader = new PdfReader(Files.newInputStream(Paths.get(public_file + "\\" + newName)));
            
            // 获取总页数
            int pages = pdfReader.getNumberOfPages();
//            log.info("{}",pages);
            // 同文件多页 打印 就多次调用 printFile方法
            // i 为页码  当找不到时就停止打印了  ， 这里动态获取页码
            if (numberOfPrintedPagesIndex.equals("all")) {
                PdfPrintUtil.printFile(public_file + "\\" + newName, "Brother HL-2240D series", newName, pages, numberOfPrintedPages, printingDirection, printBigValue);
                try {
                    Printer printer = new Printer();
                    printer.setName(oldName);
                    printer.setPrintingDirection(printingDirection);
                    printer.setNumberOfPrintedPages(numberOfPrintedPages);
                    printer.setPrintBigValue(printBigValue);
                    printer.setNumberOfPrintedPagesIndex(numberOfPrintedPagesIndex);
                    printer.setCreateUser(userId);
                    printerService.addPrinter(printer);
                } catch (Exception e) {
                    // 捕获异常，重在打印，记录没记上算了
                    log.error("捕获异常:{}", e.getMessage());
                }
                return true;
            } else {
                PdfPrintUtil.printFile(public_file + "\\" + newName, "Brother HL-2240D series", newName, Integer.parseInt(numberOfPrintedPagesIndex), numberOfPrintedPages, printingDirection, printBigValue);
                try {
                    Printer printer = new Printer();
                    printer.setName(oldName);
                    printer.setPrintingDirection(printingDirection);
                    printer.setNumberOfPrintedPages(numberOfPrintedPages);
                    printer.setPrintBigValue(printBigValue);
                    printer.setNumberOfPrintedPagesIndex(numberOfPrintedPagesIndex);
                    printer.setCreateUser(userId);
                    printerService.addPrinter(printer);
                } catch (Exception e) {
                    // 捕获异常，重在打印，记录没记上算了
                    log.error("捕获异常:{}", e.getMessage());
                }
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
    }
    
    
    @Transactional
    @Override
    public boolean printsForWord(String newName, String originName, Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex, Long userId) {
        checkBeforePrint(numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex);
        try {
            String suffix = StringUtils.substringAfter(newName, ".");// 后缀
            String newNamePDF = UUID.randomUUID().toString() + ".pdf";
            WordPrintUtil.wordToPDF(public_file + "\\" + newName, public_file + "\\" + newNamePDF);
            // 后面就调用pdf打印就行
            return printsForPDF(newNamePDF, originName, numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex, userId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    

}
