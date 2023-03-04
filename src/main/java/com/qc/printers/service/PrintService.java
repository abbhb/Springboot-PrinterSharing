package com.qc.printers.service;


import com.qc.printers.pojo.entity.Printer;

public interface PrintService {

    boolean printsForPDF(String newName, String oldName, Integer numberOfPrintedPages,Integer printingDirection);
}
