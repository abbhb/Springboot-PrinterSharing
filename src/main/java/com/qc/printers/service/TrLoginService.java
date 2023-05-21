package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.TrLogin;

public interface TrLoginService extends IService<TrLogin> {

    R<UserResult> casLogin(String code);

}
