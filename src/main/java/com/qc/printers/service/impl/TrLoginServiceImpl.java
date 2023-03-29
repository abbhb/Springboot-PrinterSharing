package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.mapper.TrLoginMapper;
import com.qc.printers.pojo.entity.TrLogin;
import com.qc.printers.service.TrLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 第三方登录服务之ENROOM
 */
@Service
@Slf4j
public class TrLoginServiceImpl extends ServiceImpl<TrLoginMapper, TrLogin> implements TrLoginService {

}
