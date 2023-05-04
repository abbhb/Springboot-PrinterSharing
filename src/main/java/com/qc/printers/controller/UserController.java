package com.qc.printers.controller;

import com.qc.printers.common.MyString;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.Token;
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
    private final CASOauthUtil casOauthUtil;

    public UserController(UserService userService, CASOauthUtil casOauthUtil) {
        this.userService = userService;
        this.casOauthUtil = casOauthUtil;
    }


    /**
     * 2023-04-22 13:29:25 升级此接口为CAS认证
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "")
    public R<UserResult> login(@RequestBody Map<String, Object> ticket,HttpServletResponse response){
        /**
         * 对密码进行加密传输
         */
        String st = (String) ticket.get("st");
        if (StringUtils.isEmpty(st)){
            return R.error("认证失败");
        }
        R<UserResult> login = userService.login(st);
        CookieManger.setARCookie(response,login.getData().getAccessToken(),login.getData().getRefreshToken());
        return login;
    }

    @PostMapping("/loginbytoken")
    @ApiOperation(value = "token校验",notes = "没过期就等效登录")
    public R<UserResult> loginByToken(HttpServletRequest request, HttpServletResponse response){
        String accessToken = casOauthUtil.cookieGetValue(request, MyString.pre_cookie_access_token);
        String refreshToken = casOauthUtil.cookieGetValue(request, MyString.pre_cookie_refresh_token);
        Token token = new Token();
       token.setAccessToken(accessToken);
       token.setRefreshToken(refreshToken);
        R<UserResult> userResultR = userService.loginByToken(token);
        if (!userResultR.getData().getAccessToken().equals(accessToken)){
            //说明期间刷新并且成功刷新了token
            CookieManger.setARCookie(response,userResultR.getData().getAccessToken(),userResultR.getData().getRefreshToken());
        }
        return userResultR;
    }
}
