package com.qc.printers.controller;

import com.qc.printers.common.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController//@ResponseBody+@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public R<UserResult> login(@RequestBody Map<String, Object> user){
        System.out.println("user = " + user);
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        return userService.login(username,password);
    }
    @NeedToken
    @PostMapping("/logout")
    public R<UserResult> logout(@RequestHeader(value="Authorization", defaultValue = "") String token){
        return userService.logout(token);
    }

    @PostMapping("/loginbytoken")
    public R<UserResult> loginByToken(@RequestHeader(value="Authorization", defaultValue = "") String token){
        return userService.loginByToken(token);
    }

    @NeedToken
    @PostMapping("/updataemployeestatus")
    public R<String> updataEmployeeStatus(@RequestHeader(value="Authorization", defaultValue = "") String token, @RequestBody Map<String, Object> employee){
        String userId = (String) employee.get("userId");//因为雪花算法，所以ID来回传递使用字符串,传回Service前转会Long
        String caozuoId = (String) employee.get("caozuoId");//操作人
        String userStatus = (String) employee.get("userStatus");
        return userService.updataUserStatus(userId,caozuoId,userStatus,token);
    }

    @NeedToken
    @PostMapping("/updataforuser")
    public R<UserResult> updataForUser(@RequestHeader(value="Authorization", defaultValue = "") String token,@RequestBody Map<String, Object> user){
        System.out.println("user = " + user);
        String id = (String) user.get("id");
        String username = (String) user.get("username");
        String name = (String) user.get("name");
        String sex = (String) user.get("sex");
        String idNumber = (String) user.get("idNumber");
        String phone = (String) user.get("phone");
        if (id==null){
            return R.error("更新失败");
        }
        if (username==null){
            return R.error("更新失败");
        }
        if (name==null){
            return R.error("更新失败");
        }
        if (sex==null){
            return R.error("更新失败");
        }if (idNumber==null){
            return R.error("更新失败");
        }
        if (phone==null){
            return R.error("更新失败");
        }
        try {
            return userService.updataForUser(Long.valueOf(id),username,name,sex,idNumber,phone,token);
        }catch (Exception e){
            return R.error("更新失败");
        }
    }


    @PostMapping("/changepassword")
    public R<UserResult> changePassword(@RequestHeader(value="Authorization", defaultValue = "") String token,@RequestBody Map<String, Object> user){
        System.out.println("user = " + user);
        String id = (String) user.get("id");
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        String newpassword = (String) user.get("newpassword");
        String checknewpassword = (String) user.get("checknewpassword");
        return userService.changePassword(id,username,password,newpassword,checknewpassword,token);
    }

    @NeedToken
    @PostMapping("/updataemployee")
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
