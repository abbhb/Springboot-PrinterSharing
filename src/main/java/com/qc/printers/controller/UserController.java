package com.qc.printers.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.ToEmail;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.CommonService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.ParamsCalibration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController//@ResponseBody+@Controller
@RequestMapping("/user")
@Api("和用户相关的接口")
@Slf4j
public class UserController {
    private final UserService userService;

    private final CommonService commonService;
    public UserController(UserService userService, CommonService commonService) {
        this.userService = userService;
        this.commonService = commonService;
    }


    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "type:0账密登录 type:1邮箱登录")
    public R<UserResult> login(@RequestBody Map<String, Object> user){
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        int type = ParamsCalibration.booleanLoginType(username);
        if (type==0){
            return userService.login(username,password);
        }else if (type==1){
            return userService.loginByEmail(username,password);
        }else {
            return R.error("不符合规则");
        }


    }
    @NeedToken
    @ApiOperation(value = "获取邮箱验证码",notes = "验证邮箱是不是本人")
    @PostMapping("/createemailcode")
    public R<String> createEmailCode(@RequestBody Map<String, Object> email,@RequestHeader(value="Authorization", defaultValue = "") String token){
        System.out.println("email = " + email);
        String emails = (String) email.get("email");
        ToEmail toEmail = new ToEmail();
        List<String> strings = new ArrayList<>();
        strings.add(emails);
        toEmail.setTos(strings.toArray(new String[strings.size()]));
        return commonService.sendEmailCode(toEmail,token);
    }
    @NeedToken
    @PostMapping("/emailwithuser")
    @ApiOperation(value = "用户绑定邮箱")
    public R<String> emailWithUser(@RequestBody Map<String, Object> email,@RequestHeader(value="Authorization", defaultValue = "") String token){
        System.out.println("email = " + email);
        String emails = (String) email.get("email");
        String code = (String) email.get("code");
        return userService.emailWithUser(emails,code,token);
    }

    @PostMapping("/create")
    @ApiOperation(value = "注册",notes = "通过此接口注册只能注册普通用户")
    public R<String> create(@RequestBody User user){
        System.out.println("user = " + user);
        return userService.createUser(user,0L);

    }

    @NeedToken
    @PostMapping("/add")
    @ApiOperation(value = "添加用户",notes = "如果是管理员权限可以添加高权限用户")
    public R<String> add(@RequestBody User user,@RequestHeader(value="Authorization", defaultValue = "") String token){
        System.out.println("user = " + user);
//        String username = (String) user.get("username");
//        String password = (String) user.get("password");
        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim id = decodedJWT.getClaim("id");
            Long userId = 0L;
            if (StringUtils.isEmpty(id.asString())){
                userId = 0L;
            }else {
                userId = Long.valueOf(id.asString());
            }
            return userService.createUser(user,userId);
        }catch (JWTDecodeException e){
            //禁止管理员注册
            return userService.createUser(user,0L);
        }catch (NullPointerException n){
            //禁止管理员注册
            return userService.createUser(user,0L);
        }


    }

    @NeedToken
    @PostMapping("/logout")
    @ApiOperation(value = "注销",notes = "清掉redis的状态")
    public R<UserResult> logout(@RequestHeader(value="Authorization", defaultValue = "") String token){
        return userService.logout(token);
    }

    @PostMapping("/loginbytoken")
    @ApiOperation(value = "token校验",notes = "没过期就等效登录")
    public R<UserResult> loginByToken(@RequestHeader(value="Authorization", defaultValue = "") String token){
        return userService.loginByToken(token);
    }

    /**
     * 后期加入权限过滤器
     * @param pageNum
     * @param pageSize
     * @param name
     * @param userId
     * @return
     */
    @NeedToken
    @GetMapping("/get")
    @ApiOperation(value = "获取用户列表",notes = "只给管理员返回")
    public R<PageData> getUserList(Integer pageNum, Integer pageSize, String name,@RequestHeader(value="userId", defaultValue = "") Long userId){
        log.info("pageNum = {},pageSize = {},name = {},userId={}",pageNum,pageSize,name,userId);
        return userService.getUserList(pageNum,pageSize,name,userId);
    }


    /**
     * 此接口需要加密
     * @param username
     * @return
     */
    @GetMapping("/hasUserName")
    @ApiOperation(value = "判断用户名是否重复",notes = "注册的时候不断校验用户名是否重复")
    public R<String> hasUserName(String username){
        return userService.hasUserName(username);
    }

    @NeedToken
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除用户")
    public R<String> deleteUsers(String id,@RequestHeader(value="Authorization", defaultValue = "") String token){
        log.info("id = {}",id);
        if (StringUtils.isEmpty(id)){
            return R.error("无操作对象");
        }
        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim uid = decodedJWT.getClaim("id");
            Long userId = Long.valueOf(uid.asString());
            if (userId==null){
                return R.error("更新失败");
            }
            return userService.deleteUsers(id,userId);
        }catch (Exception exception){
            throw new CustomException("更新失败");
        }

    }

    @NeedToken
    @PutMapping("/updatauserstatus")
    @ApiOperation(value = "更新用户状态",notes = "用于封禁账号")
    public R<String> updataUserStatus(@RequestHeader(value="Authorization", defaultValue = "") String token, @RequestBody Map<String,Object> user){
        if (StringUtils.isEmpty((String) user.get("id"))){
            return R.error("无操作对象");
        }
        if (StringUtils.isEmpty((String) user.get("status"))){
            return R.error("无操作对象");
        }
        if (StringUtils.isEmpty(token)){
            return R.error("无操作对象");
        }

        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim id = decodedJWT.getClaim("id");
            Long userId = Long.valueOf(id.asString());
            if (userId==null){
                return R.error("更新失败");
            }
            return userService.updataUserStatus((String) user.get("id"),(String) user.get("status"),userId);
        }catch (Exception exception){
            throw new CustomException("更新失败");
        }

    }

    /**
     * 更新操作
     * @param token
     * @param user
     * @return
     */
    @NeedToken
    @PutMapping("/updataforuser")
    @ApiOperation(value = "更新用户信息")
    public R<UserResult> updataForUser(@RequestHeader(value="Authorization", defaultValue = "") String token,@RequestBody User user){
        log.info("user = {}", user);

        if (user.getId()==null){
            return R.error("更新失败");
        }
        if (user.getUsername()==null){
            return R.error("更新失败");
        }
        if (user.getName()==null){
            return R.error("更新失败");
        }
        if (user.getSex()==null){
            return R.error("更新失败");
        }if (user.getStudentId()==null){
            return R.error("更新失败");
        }
        if (user.getPhone()==null){
            return R.error("更新失败");
        }
        if (user.getStatus()==null){
            return R.error("更新失败");
        }
        if (user.getPermission()==null){
            return R.error("更新失败");
        }

        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim id = decodedJWT.getClaim("id");
            Long userId = Long.valueOf(id.asString());
            if (userId==null){
                return R.error("更新失败");
            }
            return userService.updataForUser(user,userId);
        }catch (Exception exception){
            throw new CustomException("更新失败");
        }

    }


    @NeedToken
    @PutMapping("/changepassword")
    @ApiOperation(value = "更改用户密码")
    public R<UserResult> changePassword(@RequestHeader(value="Authorization", defaultValue = "") String token,@RequestBody Map<String, Object> user){
        System.out.println("user = " + user);
        String id = (String) user.get("id");
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        String newpassword = (String) user.get("newpassword");
        String checknewpassword = (String) user.get("checknewpassword");
        return userService.changePassword(id,username,password,newpassword,checknewpassword);
    }

    @NeedToken
    @PostMapping("/updataemployee")
    @ApiOperation(value = "和上面方法重复",notes = "暂时没判断是否可以删除")
    public R<String> updataEmployee(@RequestHeader(value="Authorization", defaultValue = "") String token,@RequestBody Map<String, Object> employee){
//        String userId = (String) employee.get("userId");//因为雪花算法，所以ID来回传递使用字符串,传回Service前转会Long
        // caozuoId//操作者id
//        return userService.deleteEmployee(userId,caozuoId,token);
        // return userService.updataEmployee(caozuoId,)
        log.info(employee.toString());
        String name = (String) employee.get("name");

        String username = (String) employee.get("username");
        String phone = (String) employee.get("phone");
        String idNumber = (String) employee.get("idNumber");
        String status = (String) employee.get("status");
        String grouping = (String) employee.get("grouping");
        String sex = (String) employee.get("sex");
        String userid = (String) employee.get("userid");
        return userService.updataUser(userid,name,username,phone,idNumber,status,grouping,sex,token);
    }
}
