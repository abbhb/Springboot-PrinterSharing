package com.qc.printers.controller;

import com.alibaba.fastjson.JSONObject;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.service.RedirectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//@ResponseBody+@Controller
@RequestMapping("/redirect")
@Slf4j
public class RedirectController {
    private final RedirectService redirectService;

    @Autowired
    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }


    @GetMapping("/en")
    public R<UserResult> enRedirect(String code){
        //直接返回user下的login
        log.info("code = {}",code);
        if (StringUtils.isEmpty(code)){
            return R.error("空");
        }
        return redirectService.enRedirect(code);
    }
}
