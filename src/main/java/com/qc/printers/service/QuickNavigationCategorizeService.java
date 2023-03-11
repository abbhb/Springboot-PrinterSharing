package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.common.R;
import com.qc.printers.pojo.QuickNavigationCategorizeResult;
import com.qc.printers.pojo.QuickNavigationItemResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.QuickNavigationCategorize;
import com.qc.printers.pojo.selectOptionsResult;

import java.util.List;

public interface QuickNavigationCategorizeService extends IService<QuickNavigationCategorize> {
    R<PageData<QuickNavigationCategorizeResult>> listNavFenLei(Integer pageNum, Integer pageSize, String name);

    R<String> updataForQuickNavigationCategorize(QuickNavigationCategorize quickNavigation);

    R<String> deleteNavigationCategorize(String id);


    R<List<selectOptionsResult>> getCategorizeSelectOptionsList();

    R<String> createNavCategorize(QuickNavigationCategorize quickNavigationCategorize);
}
