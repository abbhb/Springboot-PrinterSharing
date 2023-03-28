package com.qc.printers.controller;

import com.alibaba.fastjson.JSONObject;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.service.RedirectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/firsten")
    public R<UserResult> firstEN(@RequestBody Map<String,Object> data){
        //直接返回user下的login
        log.info("data = {}",data);
        if (data.isEmpty()){
            return R.error("你倒是填写表单啊");
        }
        if (StringUtils.isEmpty((String) data.get("trId"))){
            return R.error("你倒是填写表单啊");
        }
        if (StringUtils.isEmpty((String) data.get("type"))){
            return R.error("你倒是填写表单啊");
        }
        if (StringUtils.isEmpty((String) data.get("username"))){
            return R.error("你倒是填写表单啊");
        }
        if (StringUtils.isEmpty((String) data.get("password"))){
            return R.error("你倒是填写表单啊");
        }
        Long trId = Long.valueOf((String) data.get("trId"));
        Integer type = Integer.valueOf((String) data.get("type"));
        String username = (String) data.get("username");
        String password = (String) data.get("password");

        return redirectService.firstEN(trId,type,username,password);
    }
}
