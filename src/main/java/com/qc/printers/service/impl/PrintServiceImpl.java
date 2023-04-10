package com.qc.printers.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.qc.printers.common.CustomException;
import com.qc.printers.config.MinIoProperties;
import com.qc.printers.service.CommonService;
import com.qc.printers.service.PrintService;
import com.qc.printers.service.PrinterService;
import com.qc.printers.utils.MinIoUtil;
import com.qc.printers.utils.PdfPrintUtil;
import com.qc.printers.utils.RandomName;
import com.qc.printers.utils.WordPrintUtil;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.qc.printers.common.MyString.public_file;
import static com.qc.printers.utils.ParamsCalibration.checkBeforePrint;
import static com.qc.printers.utils.ParamsCalibration.somePrinterParams;

@Service
@Slf4j
public class PrintServiceImpl implements PrintService {
    
    private final PrinterService printerService;

    private final MinIoProperties minIoProperties;
    @Autowired
    public PrintServiceImpl(PrinterService printerService, CommonService commonService, MinIoProperties minIoProperties) {
        this.printerService = printerService;
        this.minIoProperties = minIoProperties;
    }
    
    @Transactional
    @Override
    public boolean printsForPDF(String newName, String oldName, Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex, @ApiParam("是否双面打印") Integer isDuplex, Long userId) {
        checkBeforePrint(numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex);
        if (numberOfPrintedPagesIndex.contains("-")){
            String[] split = numberOfPrintedPagesIndex.split("-");
            numberOfPrintedPagesIndex = split[1];
        }
        InputStream inputStream = null;
        try {
            URL fileURL = new URL(newName);//此处newName为文件链接url
            inputStream = fileURL.openStream();
            if (inputStream==null){
                throw new CustomException("打印:流");
            }
            PdfReader pdfReader = new PdfReader(inputStream);
            
            // 获取总页数
            int pages = pdfReader.getNumberOfPages();

            // 同文件多页 打印 就多次调用 printFile方法
            // i 为页码  当找不到时就停止打印了  ， 这里动态获取页码
            if (numberOfPrintedPagesIndex.equals("all")||numberOfPrintedPagesIndex.equals("")) {
                PdfPrintUtil.printFile(newName, "Brother HL-2240D series", newName, pages, numberOfPrintedPages, printingDirection, printBigValue,isDuplex);
                try {
                    //打印记录-->后期升级为rabbitmq
                    printerService.addPrinter(somePrinterParams(oldName,newName, printingDirection, numberOfPrintedPages, printBigValue, String.valueOf(pages),isDuplex ,userId),newName);
                } catch (Exception e) {
                    // 捕获异常，重在打印，记录没记上算了
                    log.error("捕获异常:{}", e.getMessage());
                }
                return true;
            } else {
                if (numberOfPrintedPagesIndex.contains("-")){
                    numberOfPrintedPagesIndex = numberOfPrintedPagesIndex.split("-")[1];
                }
                PdfPrintUtil.printFile(newName, "Brother HL-2240D series", newName, Integer.parseInt(numberOfPrintedPagesIndex), numberOfPrintedPages, printingDirection, printBigValue,isDuplex);
                try {
                    //打印记录
                    printerService.addPrinter(somePrinterParams(oldName,newName, printingDirection, numberOfPrintedPages, printBigValue, numberOfPrintedPagesIndex, isDuplex, userId),newName);
                } catch (Exception e) {
                    // 捕获异常，重在打印，记录没记上算了
                    log.error("捕获异常:{}", e.getMessage());
                }
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("捕获异常:{}", e.getMessage());
            return false;
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                }catch (IOException exp){
                    log.error("捕获异常:{}", exp.getMessage());
                }

            }

        }
        
    }


    /**
     * 此处的newName是真的name
     * @param newName
     * @param originName
     * @param numberOfPrintedPages
     * @param printingDirection
     * @param printBigValue
     * @param numberOfPrintedPagesIndex
     * @param isDuplex
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public boolean printsForWord(String newName,String bakUrl, String originName, Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex, Integer isDuplex,Long userId) {
        checkBeforePrint(numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex);
        try {
            WordPrintUtil.wordToPDF(newName);
            String temURL = MinIoUtil.upload(minIoProperties.getBucketName(),RandomName.getRandomName(newName)+".pdf",public_file + "\\" + newName + ".pdf");
            log.info("imageUrl={}",temURL);
            String[] split = temURL.split("\\?");

            String fileURL = split[0];
            log.info("路径为:{}",fileURL);
            if (StringUtils.isEmpty(fileURL)){
                throw new CustomException("打印失败:commonService.uploadFileTOMinio(file);");
            }
            // 后面就调用pdf打印就行
            return printsForPDF(fileURL, originName, numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex, isDuplex, userId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //把打印任务交给python尝试
            String url = "http://127.0.0.1:23456/"+"?url="+bakUrl;
            //         请求客户端
            RestTemplate client = new RestTemplate();
            //      发起请求
            String body = client.getForEntity(url, String.class).getBody();
            System.out.println("******** Get请求 *********");
            assert body != null;
            return printsForPDF(body, originName, numberOfPrintedPages, printingDirection, printBigValue, numberOfPrintedPagesIndex, isDuplex, userId);

        }
    }
    

}
