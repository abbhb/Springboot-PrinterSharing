package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.mapper.QuickNavigationItemMapper;
import com.qc.printers.pojo.QuickNavigationItemResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.QuickNavigationCategorize;
import com.qc.printers.pojo.entity.QuickNavigationItem;

import com.qc.printers.service.QuickNavigationCategorizeService;
import com.qc.printers.service.QuickNavigationItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class QuickNavigationItemServiceImpl extends ServiceImpl<QuickNavigationItemMapper, QuickNavigationItem> implements QuickNavigationItemService {

    private QuickNavigationCategorizeService quickNavigationCategorizeService;
    @Autowired
    public void setQuickNavigationCategorizeService(QuickNavigationCategorizeService quickNavigationCategorizeService){
        this.quickNavigationCategorizeService = quickNavigationCategorizeService;
    }
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

    @Override
    public R<PageData<QuickNavigationItemResult>> listNavFenLeiItem(Integer pageNum, Integer pageSize, String name) {
        if (pageNum==null){
            return R.error("传参错误");
        }
        if (pageSize==null){
            return R.error("传参错误");
        }
        Page<QuickNavigationItem> pageInfo = new Page(pageNum,pageSize);
        LambdaQueryWrapper<QuickNavigationItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(name),QuickNavigationItem::getName,name);
        super.page(pageInfo,lambdaQueryWrapper);


        PageData<QuickNavigationItemResult> pageData = new PageData<>();
        List<QuickNavigationItemResult> results = new ArrayList<>();
        for (Object quickNavigationItem : pageInfo.getRecords()) {
            QuickNavigationItem quickNavigationItem1 = (QuickNavigationItem) quickNavigationItem;

            QuickNavigationItemResult quickNavigationItemResult = new QuickNavigationItemResult();
            quickNavigationItemResult.setName(quickNavigationItem1.getName());
            quickNavigationItemResult.setId(String.valueOf(quickNavigationItem1.getId()));
            quickNavigationItemResult.setIntroduction(quickNavigationItem1.getIntroduction());
            quickNavigationItemResult.setPath(quickNavigationItem1.getPath());
            quickNavigationItemResult.setImage(quickNavigationItem1.getImage());
            String[] split = quickNavigationItem1.getPermission().split(",");
            List<Integer> list = new ArrayList<>();
            for (String s:
                    split) {
                list.add(Integer.valueOf(s));
            }
            quickNavigationItemResult.setPermission(list);
            quickNavigationItemResult.setCategorizeId(String.valueOf(quickNavigationItem1.getCategorizeId()));


            /*
            * 后期这种简单粗暴可以优化成map里找
            * */
            log.info("quickNavigationCategorizeService = {}",quickNavigationCategorizeService);
            QuickNavigationCategorize quickNavigationCategorize = quickNavigationCategorizeService.getById(quickNavigationItem1.getCategorizeId());
            log.info("quickNavigationCategorize = {}",quickNavigationCategorize);
            if (quickNavigationCategorize==null){
                throw new CustomException("运行异常");
            }
            quickNavigationItemResult.setCategorizeName(quickNavigationCategorize.getName());
            results.add(quickNavigationItemResult);
        }
        pageData.setPages(pageInfo.getPages());
        pageData.setTotal(pageInfo.getTotal());
        pageData.setCountId(pageInfo.getCountId());
        pageData.setCurrent(pageInfo.getCurrent());
        pageData.setSize(pageInfo.getSize());
        pageData.setRecords(results);
        pageData.setMaxLimit(pageInfo.getMaxLimit());
        return R.success(pageData);
    }
}
