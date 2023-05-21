package com.qc.printers.controller;

import com.qc.printers.common.MyString;
import com.qc.printers.common.R;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.Token;
import com.qc.printers.service.TrLoginService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.CASOauthUtil;
import com.qc.printers.utils.CookieManger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController//@ResponseBody+@Controller
@RequestMapping("/user")
@Api("和用户相关的接口")
@CrossOrigin("*")
@Slf4j
public class UserController {
    private final UserService userService;

    private final TrLoginService trLoginService;
    private final CASOauthUtil casOauthUtil;

    public UserController(UserService userService, TrLoginService trLoginService, CASOauthUtil casOauthUtil) {
        this.userService = userService;
        this.trLoginService = trLoginService;
        this.casOauthUtil = casOauthUtil;
    }


    /**
     * 2023-04-22 13:29:25 升级此接口为CAS认证
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "")
    public R<UserResult> login(@RequestBody Map<String, Object> ticket,HttpServletResponse response){
        log.info("ticket:{}",ticket);
        /**
         * 对密码进行加密传输
         */
        String code = (String) ticket.get("code");
        if (StringUtils.isEmpty(code)){
            return R.error("认证失败");
        }
        return trLoginService.casLogin(code);
    }

    @NeedToken
    @PostMapping("/loginbytoken")
    @ApiOperation(value = "token校验",notes = "没过期就等效登录")
    public R<UserResult> loginByToken(){
        R<UserResult> userResultR = userService.loginByToken();
        return userResultR;
    }
}
