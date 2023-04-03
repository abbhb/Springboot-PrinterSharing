package com.qc.printers.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValueLabelResult implements Serializable {
    //门店选择器获取列表
    public String value;//返回都是返回字符串storeId

    public String label;//storeName(ID:storeId)
}