package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.pojo.entity.Printer;

/**
 * 专用于记录
 */
public interface PrinterService extends IService<Printer> {

    boolean addPrinter(Printer printer,String urlName);
}
