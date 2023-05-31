package com.qc.printers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.pojo.vo.CountTop10VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PrinterMapper extends BaseMapper<Printer> {
    public List<CountTop10VO> getCountTop10();

    public List<CountTop10VO> getCountTop10EveryDay();

    public Integer getPrintCount();
}
