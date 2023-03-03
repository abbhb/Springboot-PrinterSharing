package com.qc.printers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.R;
import com.qc.printers.mapper.UserMapper;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.IStringRedisService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.PWDMD5;
import com.qc.printers.utils.RandomName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final IStringRedisService iStringRedisService;

    @Autowired
    public UserServiceImpl(IStringRedisService iStringRedisService) {
        this.iStringRedisService = iStringRedisService;
    }

    @Override
    public R<UserResult> login(String username, String password) {
        if (username==null||username.equals("")){
            return R.error("用户名不存在");
        }
        if (password==null||password.equals("")){
            return R.error("密码不存在");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User one = super.getOne(queryWrapper);
        if (one==null){
            return R.error("用户名或密码错误");
        }
        String salt = one.getSalt();
        if (!PWDMD5.getMD5Encryption(password,salt).equals(one.getPassword())){//前端传入的明文密码加上后端的盐，处理后跟库中密码比对，一样登陆成功
            return R.error("用户名或密码错误");
        }

        if(one.getStatus() == 0){
            return R.error("账号已禁用!");
        }
        //jwt生成token，token里面有userid，redis里存uuid
        String uuid = RandomName.getUUID();//uuid作为key

        String token = JWTUtil.getToken(String.valueOf(one.getId()),String.valueOf(one.getGrouping()),uuid);
        iStringRedisService.setTokenWithTime(uuid, String.valueOf(one.getId()),3600L);//token作为value，id是不允许更改的
        UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),one.getIdNumber(),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getGrouping(),token);
        return R.success(UserResult);
    }

    @Override
    public R<UserResult> logout(String token) {
        return null;
    }

    @Override
    public R<UserResult> loginByToken(String token) {
        return null;
    }

    @Override
    public R<String> updataUserStatus(String userId, String caozuoId, String userStatus, String token) {
        return null;
    }

    @Override
    public R<UserResult> updataForUser(Long valueOf, String username, String name, String sex, String idNumber, String phone, String token) {
        return null;
    }

    @Override
    public R<UserResult> changePassword(String id, String username, String password, String newpassword, String checknewpassword, String token) {
        return null;
    }

    @Override
    public R<String> updataUser(String userid, String name, String username, String phone, String idNumber, String status, String grouping, String sex, String token) {
        return null;
    }
}
