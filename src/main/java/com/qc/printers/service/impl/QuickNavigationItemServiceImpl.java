package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.mapper.QuickNavigationItemMapper;
import com.qc.printers.pojo.entity.QuickNavigationItem;

import com.qc.printers.service.QuickNavigationItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QuickNavigationItemServiceImpl extends ServiceImpl<QuickNavigationItemMapper, QuickNavigationItem> implements QuickNavigationItemService {


    @Override
    public boolean hasId(Long valueOf) {
        if (valueOf==null){
            return false;
        }
        LambdaQueryWrapper<QuickNavigationItem> quickNavigationItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
        quickNavigationItemLambdaQueryWrapper.eq(QuickNavigationItem::getCategorizeId,Long.valueOf(valueOf));
        List<QuickNavigationItem> list = super.list(quickNavigationItemLambdaQueryWrapper);
        if (list.size()!=0){
            return true;
        }
        return false;
    }
}
