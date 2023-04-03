package com.qc.printers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.printers.pojo.entity.Printer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PrinterMapper extends BaseMapper<Printer> {
    public List<Printer> getCountTop10();
}
