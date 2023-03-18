package com.qc.printers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.printers.pojo.entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
}
