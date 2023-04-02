package com.qc.printers.service;


import com.qc.printers.pojo.entity.Printer;

public interface PrintService {

    boolean printsForPDF(String newName, String oldName, Integer numberOfPrintedPages,Integer printingDirection,Integer printBigValue,String numberOfPrintedPagesIndex,Integer isDuplex,Long userId);

    boolean printsForWord(String newName, String originName, Integer numberOfPrintedPages, Integer printingDirection, Integer printBigValue, String numberOfPrintedPagesIndex,Integer isDuplex,Long userId);
}
