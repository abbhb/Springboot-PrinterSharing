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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
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
        Page<QuickNavigationItem> pageInfo = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<QuickNavigationItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(name),QuickNavigationItem::getName,name);
        super.page(pageInfo,lambdaQueryWrapper);

        log.info("pageInfo = {}",pageInfo);

        PageData<QuickNavigationItemResult> pageData = new PageData<>();
        List<QuickNavigationItemResult> results = new ArrayList<>();
        for (QuickNavigationItem quickNavigationItem : pageInfo.getRecords()) {

            log.info("quickNavigationItem = {}",quickNavigationItem);
            QuickNavigationItemResult quickNavigationItemResult = new QuickNavigationItemResult();
            quickNavigationItemResult.setName(quickNavigationItem.getName());
            quickNavigationItemResult.setId(String.valueOf(quickNavigationItem.getId()));
            quickNavigationItemResult.setIntroduction(quickNavigationItem.getIntroduction());
            quickNavigationItemResult.setPath(quickNavigationItem.getPath());
            quickNavigationItemResult.setImage(quickNavigationItem.getImage());
            String[] split = quickNavigationItem.getPermission().split(",");
            List<Integer> list = new ArrayList<>();
            for (String s:
                    split) {
                list.add(Integer.valueOf(s));
            }
            quickNavigationItemResult.setPermission(list);
            quickNavigationItemResult.setCategorizeId(String.valueOf(quickNavigationItem.getCategorizeId()));


            /*
            * 后期这种简单粗暴可以优化成map里找
            * */
            QuickNavigationCategorize quickNavigationCategorize = quickNavigationCategorizeService.getById(quickNavigationItem.getCategorizeId());
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

    @Transactional
    @Override
    public R<String> createNavItem(QuickNavigationItem quickNavigationItem) {
        if(StringUtils.isEmpty(quickNavigationItem.getName())){
            throw new CustomException("必参缺少");
        }
        if(StringUtils.isEmpty(quickNavigationItem.getPath())){
            throw new CustomException("必参缺少");
        }
        if(quickNavigationItem.getCategorizeId()==null){
            throw new CustomException("必参缺少");
        }
        if(StringUtils.isEmpty(quickNavigationItem.getPermission())){
            throw new CustomException("必参缺少");
        }

        //权限存入数据库必须改逗号分隔格式
        if (quickNavigationItem.getPermission().equals("2")){
            quickNavigationItem.setPermission("1,2");
        }
        boolean save = super.save(quickNavigationItem);
        if (save){
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    @Override
    public R<String> deleteNavigationItem(String id) {
        if (StringUtils.isEmpty(id)){
            return R.error("无操作对象");
        }
        Collection<Long> ids = new ArrayList<>();
        if (id.contains(",")){
            String[] split = id.split(",");
            for (String s:
                    split) {

                ids.add(Long.valueOf(s));
            }
            super.removeByIds(ids);
        }else {

            LambdaQueryWrapper<QuickNavigationItem> lambdaUpdateWrapper = new LambdaQueryWrapper<>();
            lambdaUpdateWrapper.eq(QuickNavigationItem::getId,Long.valueOf(id));
            super.remove(lambdaUpdateWrapper);
        }

        return R.success("删除成功");
    }
}
