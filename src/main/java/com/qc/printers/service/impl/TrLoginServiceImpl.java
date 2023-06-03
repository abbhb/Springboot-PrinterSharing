package com.qc.printers.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.Code;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.mapper.TrLoginMapper;
import com.qc.printers.pojo.entity.Token;
import com.qc.printers.pojo.entity.TrLogin;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.pojo.vo.LoginRes;
import com.qc.printers.service.IRedisService;
import com.qc.printers.service.TrLoginService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.CASOauthUtil;
import com.qc.printers.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * 第三方登录服务之ENROOM
 */
@Service
@Slf4j
public class TrLoginServiceImpl extends ServiceImpl<TrLoginMapper, TrLogin> implements TrLoginService {
    @Autowired
    private CASOauthUtil casOauthUtil;
    private final RestTemplate restTemplate;

    private final IRedisService iRedisService;
    @Autowired
    private UserService userService;

    public TrLoginServiceImpl(RestTemplate restTemplate, IRedisService iRedisService) {
        this.restTemplate = restTemplate;
        this.iRedisService = iRedisService;
    }

    @Transactional
    @Override
    public R<LoginRes> casLogin(String code) {
        if (code==null||code.equals("")){
            throw new CustomException("认证失败", Code.DEL_TOKEN);
        }
        Token tokenByCode = casOauthUtil.getTokenByCode(restTemplate, code);
        if (tokenByCode==null){
            throw new CustomException("认证失败",Code.DEL_TOKEN);
        }
        JSONObject userObjectByToken = casOauthUtil.getUserObjectByToken(restTemplate, tokenByCode);
        if (userObjectByToken==null){
            throw new CustomException("认证失败",Code.DEL_TOKEN);
        }
        String openid = userObjectByToken.getString("openid");
        LambdaQueryWrapper<TrLogin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrLogin::getTrId,openid);
        TrLogin one = this.getOne(wrapper);
        if (one!=null&&one.getStatus().equals(1)){
            //已经绑定
            User byId = userService.getById(one.getUserId());
            if (byId==null){
                throw new CustomException("认证失败",Code.DEL_TOKEN);
            }
            String token = JWTUtil.getToken(String.valueOf(byId.getId()), String.valueOf(byId.getPermission()));
            iRedisService.setTokenWithTime(token,String.valueOf(byId.getId()),3*3600L);
            LoginRes userResult = new LoginRes();
            userResult.setToken(token);
            return R.success(userResult);
        }
        //下面是新建账号,后面改为自定义信息
        User user = new User();
        //正常是不会出现用户名重复的情况的
        user.setUsername(userObjectByToken.getString("username"));
        user.setName(userObjectByToken.getString("name"));
        String avatar = null;
        String phone;
        try{
            String string = userObjectByToken.getString("avatar");
            avatar = string;
        }catch (Exception e){
            avatar = null;
        }
        try {
            phone = userObjectByToken.getString("phone");
        }catch (Exception e){
            phone = null;
        }
        user.setPhone(phone);

        user.setAvatar(avatar);
        user.setStatus(1);
        user.setSex(userObjectByToken.getString("sex"));
        user.setEmail(userObjectByToken.getString("email"));
        String permissionName = userObjectByToken.getString("permission_name");
        if (StringUtils.isEmpty(permissionName)){
            user.setPermission(2);
        }else {
            if (permissionName.equals("管理员")){
                user.setPermission(1);
            }else if (permissionName.equals("用户")){
                user.setPermission(2);
            }else if (permissionName.equals("系统管理员")){
                user.setPermission(10);
            }else {
                user.setPermission(2);
            }
        }
        boolean save = userService.save(user);
        if (save){
            if (one==null){
                //添加账号并绑定然后登录
                TrLogin trLogin = new TrLogin();
                trLogin.setStatus(1);
                trLogin.setTrId(openid);
                trLogin.setType(2);
                trLogin.setIsDeleted(0);
                trLogin.setUserId(user.getId());
                boolean save1 = this.save(trLogin);
                if(!save1){
                    throw new CustomException("认证失败",Code.DEL_TOKEN);
                }
            }else if (one.getStatus().equals(0)){
                LambdaUpdateWrapper<TrLogin> trLoginLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                trLoginLambdaUpdateWrapper.eq(TrLogin::getUserId,user.getId());
                //修改状态为1
                trLoginLambdaUpdateWrapper.set(TrLogin::getStatus,1);
                boolean update = this.update(trLoginLambdaUpdateWrapper);
                if (!update){
                    throw new CustomException("认证失败",Code.DEL_TOKEN);
                }
            }
            String token = JWTUtil.getToken(String.valueOf(user.getId()), String.valueOf(user.getPermission()));
            iRedisService.setTokenWithTime(token,String.valueOf(user.getId()),3*3600L);
            LoginRes userResult = new LoginRes();
            userResult.setToken(token);
            return R.success(userResult);
        }
        return R.error("错误");
    }
}
