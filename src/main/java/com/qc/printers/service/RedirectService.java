package com.qc.printers.service;

import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;

public interface RedirectService {

    R<UserResult> enRedirect(String code);

    R<UserResult> firstEN(Long trId, Integer type, String username, String password);
}
