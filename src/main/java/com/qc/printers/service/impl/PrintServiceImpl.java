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
    public boolean printsForPDF(String newName, String oldName, Integer copies, Integer printingDirection, Integer printBigValue, Integer needPrintPagesEndIndex, @ApiParam("是否双面打印") Integer isDuplex, Long userId) {
        checkBeforePrint(copies, printingDirection, printBigValue, needPrintPagesEndIndex);

        InputStream inputStream = null;
        try {
            URL fileURL = new URL(newName);// 此处newName为文件链接url
            inputStream = fileURL.openStream();
            if (inputStream == null) {
                throw new CustomException("打印:流");
            }
            PdfReader pdfReader = new PdfReader(inputStream);
            
            // 获取总页数
            int pages = pdfReader.getNumberOfPages();
            // 同文件多页 打印 就多次调用 printFile方法
            // i 为页码  当找不到时就停止打印了, 这里动态获取页码
            //needPrintPagesEndIndex==-1就是全打印
            if (needPrintPagesEndIndex==-1) {
                PdfPrintUtil.printFile(newName, "Brother HL-2240D series", newName, pages, copies, printingDirection, printBigValue, isDuplex);
                try {
                    // 打印记录-->后期升级为rabbitmq

                    printerService.addPrinter(somePrinterParams(oldName, newName, printingDirection, copies, printBigValue, pages, isDuplex,pages,pages, userId), newName);
                } catch (Exception e) {
                    // 捕获异常，重在打印，记录没记上算了
                    log.error("捕获异常:{}", e.getMessage());
                }
                return true;
            } else {
                PdfPrintUtil.printFile(newName, "Brother HL-2240D series", newName, needPrintPagesEndIndex, copies, printingDirection, printBigValue, isDuplex);
                try {
                    // 打印记录
                    printerService.addPrinter(somePrinterParams(oldName, newName, printingDirection, copies, printBigValue, needPrintPagesEndIndex, isDuplex,pages,needPrintPagesEndIndex, userId), newName);
                } catch (Exception e) {
                    // 捕获异常，重在打印，记录没记上算了
                    log.error("捕获异常:{}", e.getMessage());
                }
                return true;
            }
        } catch (IOException e) {
            log.error("捕获异常:{}", e.getMessage());
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exp) {
                    log.error("捕获异常:{}", exp.getMessage());
                }
                
            }
            
        }
        
    }


    /**
     * 此处的newName是真的name
     * @param newName 真实文件名
     * @param bakUrl 备份url
     * @param originName 源文件名[用来记录]
     * @param copies 份数
     * @param printingDirection 打印方向
     * @param printBigValue 打印大小
     * @param needPrintPagesEndIndex 需要打印到那一页 末页页码
     * @param isDuplex 是否双面打印
     * @param userId 用户id
     * @return
     */
    @Transactional
    @Override
    public boolean printsForWord(String newName, String bakUrl, String originName, Integer copies, Integer printingDirection, Integer printBigValue, Integer needPrintPagesEndIndex, Integer isDuplex, Long userId) {
        //校验参数
        checkBeforePrint(copies, printingDirection, printBigValue, needPrintPagesEndIndex);
        try {
            //此处会自动把文件转换为pdf并且上传minio
            WordPrintUtil.wordToPDF(newName);
            String temURL = MinIoUtil.upload(minIoProperties.getBucketName(), RandomName.getRandomName(newName) + ".pdf", public_file + "\\" + newName + ".pdf");
            String[] split = temURL.split("\\?");
            String fileURL = split[0];
            if (StringUtils.isEmpty(fileURL)) {
                throw new CustomException("打印失败:commonService.uploadFileTOMinio(file);");
            }
            // 打印的就是pdf,传入pdf文件地址就行
            return printsForPDF(fileURL, originName, copies, printingDirection, printBigValue, needPrintPagesEndIndex, isDuplex, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 把打印任务交给python再尝试
            String url = "http://127.0.0.1:23456/" + "?url=" + bakUrl;
            //         请求客户端
            RestTemplate client = new RestTemplate();
            //      发起请求
            String body = client.getForEntity(url, String.class).getBody();
            assert body != null;
            //如果返回了内容[url]则打印
            return printsForPDF(body, originName, copies, printingDirection, printBigValue, needPrintPagesEndIndex, isDuplex, userId);
        }
    }
    
    
}
