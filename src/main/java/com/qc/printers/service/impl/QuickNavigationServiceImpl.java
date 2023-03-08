package com.qc.printers.service.impl;


import com.qc.printers.common.R;
import com.qc.printers.pojo.QuickNavigationItemResult;
import com.qc.printers.pojo.QuickNavigationResult;
import com.qc.printers.pojo.entity.QuickNavigationCategorize;
import com.qc.printers.pojo.entity.QuickNavigationItem;
import com.qc.printers.service.QuickNavigationCategorizeService;
import com.qc.printers.service.QuickNavigationItemService;
import com.qc.printers.service.QuickNavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuickNavigationServiceImpl implements QuickNavigationService {

    private final QuickNavigationItemService quickNavigationItemService;
    private final QuickNavigationCategorizeService quickNavigationCategorizeService;

    @Autowired
    public QuickNavigationServiceImpl(QuickNavigationItemService quickNavigationItemService, QuickNavigationCategorizeService quickNavigationCategorizeService) {
        this.quickNavigationItemService = quickNavigationItemService;
        this.quickNavigationCategorizeService = quickNavigationCategorizeService;
    }

    @Override
    public R<List<QuickNavigationResult>> list(Long userId) {
        if (userId==null){
            return R.error("异常");
        }
        List<QuickNavigationCategorize> quickNavigationCategorizes = quickNavigationCategorizeService.list();
        List<QuickNavigationItem> quickNavigationItems = quickNavigationItemService.list();
        List<QuickNavigationResult> quickNavigationResults = new ArrayList<>();


        for (QuickNavigationCategorize quickNavigationCategorize:
                quickNavigationCategorizes) {
            QuickNavigationResult quickNavigationResult = new QuickNavigationResult();
            quickNavigationResult.setId(String.valueOf(quickNavigationCategorize.getId()));
            quickNavigationResult.setName(quickNavigationCategorize.getName());
            List<QuickNavigationItem> result1 = quickNavigationItems.stream()
                    .filter(item -> item.getCategorizeId().equals(quickNavigationCategorize.getId()))
                    .collect(Collectors.toList());

            List<QuickNavigationItemResult> quickNavigationItemResults = new ArrayList<>();
            for (QuickNavigationItem quickNavigationItem:
                    result1) {
                QuickNavigationItemResult quickNavigationItemResult = new QuickNavigationItemResult();
                quickNavigationItemResult.setId(String.valueOf(quickNavigationItem.getId()));
                quickNavigationItemResult.setName(quickNavigationItem.getName());
                quickNavigationItemResult.setPath(quickNavigationItem.getPath());
                quickNavigationItemResult.setImage(quickNavigationItem.getImage());
                quickNavigationItemResult.setIntroduction(quickNavigationItem.getIntroduction());
                String permission = quickNavigationItem.getPermission();
                if (permission.contains(",")){
                    List<Integer> integerList = new ArrayList<>();
                    String[] split = permission.split(",");
                    for (String s:
                         split) {
                        integerList.add(Integer.valueOf(s));
                    }
                    quickNavigationItemResult.setPermission(integerList);
                }else {
                    List<Integer> integerList = new ArrayList<>();
                    integerList.add(Integer.valueOf(permission));
                    quickNavigationItemResult.setPermission(integerList);
                }
                quickNavigationItemResults.add(quickNavigationItemResult);
            }
            quickNavigationResult.setItem(quickNavigationItemResults);
            quickNavigationResults.add(quickNavigationResult);
        }
        return R.success(quickNavigationResults);
    }
}
