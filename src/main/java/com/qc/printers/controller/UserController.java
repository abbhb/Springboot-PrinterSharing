package com.qc.printers.controller;

import com.qc.printers.common.R;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.pojo.vo.LoginRes;
import com.qc.printers.pojo.vo.PasswordR;
import com.qc.printers.service.TrLoginService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.CASOauthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

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
     * 登录分为账密和Oauth2.0授权登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "")
    public R<LoginRes> login(@RequestBody User user){
        log.info("user:{}",user);
        /**
         * 对密码进行加密传输
         */
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)){
            return R.error("密码不能为空");
        }
        return userService.login(user);
    }

    /**
     * 2023-04-22 13:29:25 升级此接口为CAS认证
     * @return
     */
    @PostMapping("/loginbycode")
    @ApiOperation(value = "登录",notes = "")
    public R<LoginRes> loginByCode(@RequestBody Map<String, Object> ticket){
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
    @ApiOperation(value = "token校验",notes = "没过期就data返回1告诉前端一声")
    public R<Integer> loginByToken(){
        R<Integer> userResultR = userService.loginByToken();
        return userResultR;
    }

    @NeedToken
    @PutMapping("/update")
    @ApiOperation(value = "token校验",notes = "没过期就data返回1告诉前端一声")
    public R<String> update(@RequestBody User user){
        boolean isTrue = userService.updateUserInfo(user);
        if (isTrue){
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }
    @NeedToken
    @PutMapping("/setPassword")
    public R<String> setPassword(@RequestBody PasswordR passwordR){
        boolean isTrue = userService.setPassword(passwordR);
        if (isTrue){
            return R.success("修改成功,下次登录生效!");
        }
        return R.error("修改失败");
    }

    @NeedToken
    @PostMapping("/info")
    @ApiOperation(value = "获取用户信息",notes = "")
    public R<UserResult> info(){
        log.info("获取用户信息");
        return userService.info();
    }

    @GetMapping("/user_count")
    @ApiOperation(value = "获取用户数量",notes = "")
    public R<Integer> userCount(){
        log.info("获取用户数量");
        return R.success(userService.count());
    }

    @GetMapping("/user_password")
    @NeedToken
    @ApiOperation(value = "是否需要输入密码",notes = "")
    public R<Integer> userPassword(){
        log.info("是否需要输入密码");
        return R.success(userService.userPassword());
    }

}
