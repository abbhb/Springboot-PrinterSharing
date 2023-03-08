package com.qc.printers.service;

import com.qc.printers.common.R;
import com.qc.printers.pojo.QuickNavigationResult;

import java.util.List;

public interface QuickNavigationService{
    R<List<QuickNavigationResult>> list(Long userId);
}
