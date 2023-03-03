package com.qc.printers.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;

public class PdfPrintUtil {
    /**
     * @param fileUrl		文件地址
     * @param printService   打印机名称   知道的可以直接指定
     * @param jobName  文件名
     * @param pageNum  页码
     */
    public static void printFile(String fileUrl, String printService, String jobName,int pageNum) {
        File file = new File(fileUrl);
        try {
            PDDocument document = PDDocument.load(file);
            PDFPrintable printable = new PDFPrintable(document);
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("文件:"+jobName);
            job.setPrintable(printable);
//            job.setPrintService(specifyPrinter(printService));
            //job.setPageable(new PDFPageable(document));
            Paper paper = new Paper();

            // 设置打印纸张大小
//            paper.setSize(595, 842); // 值为点 1 = 1/72 inch
            // 设置打印位置与坐标
            paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
            PageFormat pageFormat = new PageFormat();
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.LANDSCAPE);// LANDSCAPE表示竖打;PORTRAIT表示横


            //如果原本内容是横的就选Landscape，原内容为竖的就portrait
            Book book = new Book();
            /**
             *实际大小 ACTUAL_SIZE,
             *缩小     SHRINK_TO_FIT,
             *拉伸     STRETCH_TO_FIT,
             *适应     SCALE_TO_FIT;
             **/
            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.SCALE_TO_FIT);

            // 页码
            book.append(pdfPrintable, pageFormat, pageNum);

            job.setPageable(book);

            //设置打印份数
            job.setCopies(1);
            //调用isCancelled()方法获取打印状态,如果打印被取消,返回true,否则返回false。
            System.out.println("打印是否被取消："+job.isCancelled());
            job.print();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取打印机服务
    public static PrintService specifyPrinter(String printerName) {
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(psInFormat, pras);
        PrintService myPrinter = null;
        // 遍历所有打印机如果没有选择打印机或找不到该打印机则调用默认打印机
        for (PrintService printService2 : printService) {

            String svcname = printService2.toString();
            System.out.println("打印机有："+svcname);
            if (svcname.contains(printerName)) {
                myPrinter = printService2;
                break;
            }
        }
        if (myPrinter == null) {
            myPrinter = PrintServiceLookup.lookupDefaultPrintService();
        }
        System.out.println("选择的打印机为："+myPrinter);
        return myPrinter;
    }
}
