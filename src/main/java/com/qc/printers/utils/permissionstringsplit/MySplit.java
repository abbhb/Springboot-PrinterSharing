package com.qc.printers.utils.permissionstringsplit;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 暂时仅支持仅admin可见内容，后续可以改为通用性的方法
 */
@Slf4j
public class MySplit {
    public static String splitString(String content,Integer permission){
        //开始的下标的KEY
        String startKey = "startIndex";
        //结束的下标的KEY
        String endKey = "endIndex";

        String zhengzetou = "⌘1⌾";

        String zhengzewei = "⌾1⌘";

        Pattern startPattern = Pattern.compile(zhengzetou);
        //正则匹配需要获取的结束的字符标记
        Pattern endPattern = Pattern.compile(zhengzewei);
        Matcher startMatcher = startPattern.matcher(content);
        //结束标记对比
        Matcher endMatcher = endPattern.matcher(content);
        List<Map<String, Integer>> mapList = new ArrayList();
        //开始标记查找
        while (startMatcher.find() && endMatcher.find()) {
            Map<String, Integer> map = new HashMap<>();
            //<small hidden="">的结束下标
            map.put(startKey, startMatcher.end());
            //</small>的开始下标
            map.put(endKey, endMatcher.start());
            mapList.add(map);
        }
        if (mapList.size()==0){
            return content;
        }
        StringBuilder newContent = new StringBuilder();
        if (permission.equals(1)||permission.equals(10)){

            int last = 0;
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Integer> tempMap = mapList.get(i);
                //根据前面获取的下标直接截取字段即可
                int i1 = tempMap.get(startKey) - zhengzetou.length();
                //i1到tempMap.get(startKey)之间为符号位
                int i2 = tempMap.get(endKey) + zhengzewei.length();
                //tempMap.get(endKey)到i2之间为符号位
                String smallValue= content.substring(tempMap.get(startKey),
                        tempMap.get(endKey));
                newContent.append(content.substring(last, i1));
                newContent.append(smallValue);
                last = i2;
            }
            //此处注意,最后一个权限符号后若有内容也得加上
            newContent.append(content.substring(last, content.length()));
            return newContent.toString();
        }else{
            int last = 0;
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Integer> tempMap = mapList.get(i);
                //根据前面获取的下标直接截取字段即可
                int i1 = tempMap.get(startKey) - zhengzetou.length();
                //i1到tempMap.get(startKey)之间为符号位
                int i2 = tempMap.get(endKey) + zhengzewei.length();
                //tempMap.get(endKey)到i2之间为符号位
                String smallValue= content.substring(tempMap.get(startKey),
                        tempMap.get(endKey));
                newContent.append(content.substring(last, i1));
                last = i2;
            }
            //此处注意,最后一个权限符号后若有内容也得加上
            newContent.append(content.substring(last, content.length()));
            return newContent.toString();

        }

    }
}
