package com.qc.printers.service.impl;

import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.service.RedirectService;
import org.springframework.stereotype.Service;

@Service
public class RedireServiceImpl implements RedirectService {

    @Override
    public R<UserResult> enRedirect(String code) {
        //首先拿code去拿信息


        //判断信息是否为注册用户

        //未注册可选择绑定或者是注册
        return null;
    }
}
