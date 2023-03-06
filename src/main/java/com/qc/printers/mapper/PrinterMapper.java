package com.qc.printers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.printers.pojo.entity.Printer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrinterMapper extends BaseMapper<Printer> {
}
