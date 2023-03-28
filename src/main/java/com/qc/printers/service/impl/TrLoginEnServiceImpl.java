package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.mapper.TrLoginEnMapper;
import com.qc.printers.pojo.entity.TrLoginEn;
import com.qc.printers.service.TrLoginEnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 第三方登录服务之ENROOM
 */
@Service
@Slf4j
public class TrLoginEnServiceImpl extends ServiceImpl<TrLoginEnMapper, TrLoginEn> implements TrLoginEnService {

}
