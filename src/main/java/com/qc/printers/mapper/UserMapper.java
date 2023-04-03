package com.qc.printers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.printers.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    public List<User> getAllUserIncludeDeleted();

    public User getUserIncludeDeleted(@Param("id") Long id);
}
