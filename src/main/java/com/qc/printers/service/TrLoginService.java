package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.common.R;
import com.qc.printers.pojo.entity.TrLogin;
import com.qc.printers.pojo.vo.LoginRes;

public interface TrLoginService extends IService<TrLogin> {

    R<LoginRes> casLogin(String code);

}
