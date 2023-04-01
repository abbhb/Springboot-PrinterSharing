package com.qc.printers.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.mapper.PrinterMapper;
import com.qc.printers.pojo.PrinterResult;
import com.qc.printers.pojo.QuickNavigationCategorizeResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.Printer;
import com.qc.printers.pojo.entity.QuickNavigationCategorize;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.PrinterService;
import com.qc.printers.utils.FileMD5;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.qc.printers.common.MyString.public_file;

@Service
@Slf4j
public class PrinterServiceImpl extends ServiceImpl<PrinterMapper, Printer> implements PrinterService {

    @Transactional
    @Override
    public boolean addPrinter(Printer printer,String urlName) {
        if (StringUtils.isEmpty(printer.getName())){
            log.error("打印记录缺失");
            return false;
        }
        if (printer.getCreateUser()==null){
            log.error("打印记录缺失");
            return false;
        }
        String fileUrl = public_file+"\\"+urlName;
        try {
            printer.setContentHash(FileMD5.md5HashCode(fileUrl));
            boolean save = super.save(printer);
            if (save){
                return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            log.error("打印记录缺失");
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public R<PageData<PrinterResult>> listPrinter(Integer pageNum, Integer pageSize, String token, String name, String date) {
        if (pageNum==null){
            return R.error("传参错误");
        }
        if (pageSize==null){
            return R.error("传参错误");
        }
        DecodedJWT decodedJWT = JWTUtil.deToken(token);
        Claim id = decodedJWT.getClaim("id");
        if (id==null){
            return R.error("缺少关键信息");
        }
        Page pageInfo = new Page(pageNum,pageSize);
        LambdaQueryWrapper<Printer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Printer::getCreateUser,Long.valueOf(id.asString()));
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(name),Printer::getName,name);
        //暂时不支持通过日期模糊查询
        Page page = super.page(pageInfo, lambdaQueryWrapper);
        if (page==null){
            return R.error("啥也没有");
        }
        PageData<PrinterResult> pageData = new PageData<>();
        List<PrinterResult> results = new ArrayList<>();
        for (Object printerItem : pageInfo.getRecords()) {
            Printer printerItem1 = (Printer) printerItem;

            PrinterResult printerResult = new PrinterResult();
            printerResult.setName(printerItem1.getName());
            printerResult.setContentHash(printerItem1.getContentHash());
            printerResult.setCreateTime(printerItem1.getCreateTime());
//            printerResult.setCreateUser(String.valueOf(printerItem1.getCreateUser())); 自己的记录肯定是自己没必要
            printerResult.setNumberOfPrintedPages(printerItem1.getNumberOfPrintedPages());
            printerResult.setId(String.valueOf(printerItem1.getId()));
            results.add(printerResult);
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
