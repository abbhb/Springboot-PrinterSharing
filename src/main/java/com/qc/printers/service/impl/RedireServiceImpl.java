package com.qc.printers.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qc.printers.common.Code;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.TrLogin;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.IStringRedisService;
import com.qc.printers.service.RedirectService;
import com.qc.printers.service.TrLoginService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.PWDMD5;
import com.qc.printers.utils.RandomName;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 回调服务
 */
@Service
public class RedireServiceImpl implements RedirectService {

    private final RestTemplate restTemplate;

    private final TrLoginService trLoginService;

    private final IStringRedisService iStringRedisService;

    private final UserService userService;

    @Autowired
    public RedireServiceImpl(RestTemplate restTemplate, TrLoginService trLoginService, IStringRedisService iStringRedisService, UserService userService) {
        this.restTemplate = restTemplate;
        this.trLoginService = trLoginService;
        this.iStringRedisService = iStringRedisService;
        this.userService = userService;
    }

    /**
     * ENOAuth登录
     * @param code
     * @return
     */
    @Transactional
    @Override
    public R<UserResult> enRedirect(String code) {
        //首先拿code去拿信息
        String url = "http://10.15.247.254/en/oauth/token";

        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        //入参
        request.set("grant_type","authorization_code");
        request.set("code", code);
        request.set("client_id", "WFIko9MEhg1BuOFDRlnGc4JvxEnhi48e2F9cr8Ud");
        request.set("client_secret", "QC4H7KsnmzNVsmvvzROA14bfsqjhBYAJvHJbHpTF");
        //请求
        String result = restTemplate.postForObject(url,request,String.class);
        System.out.println(result);
        Map map = JSON.parseObject(result, Map.class);
        String accessToken = (String) map.get("access_token");
        String url2 = "http://10.15.247.254/en/oauth/me/?access_token="+accessToken;
        //1. getForObject()
        //先获取返回的字符串，若想获取属性，可以使用gson转化为实体后get方法获取
        String result2 = restTemplate.getForObject(url2, String.class);
        Map map2 = JSON.parseObject(result2, Map.class);
        System.out.println(map2);
        String trID = (String) map2.get("ID");
        if (StringUtils.isEmpty(trID)){
            return R.error("登录失败");
        }
        LambdaQueryWrapper<TrLogin> trLoginEnLambdaQueryWrapper = new LambdaQueryWrapper<>();
        trLoginEnLambdaQueryWrapper.eq(TrLogin::getTrId,Long.valueOf(trID));
        trLoginEnLambdaQueryWrapper.eq(TrLogin::getType,1);
        TrLogin trLogin = trLoginService.getOne(trLoginEnLambdaQueryWrapper);
        if (trLogin ==null){
            TrLogin trLogin1 = new TrLogin();
            trLogin1.setTrId(Long.valueOf(trID));
            trLogin1.setStatus(0);
            trLogin1.setIsDeleted(0);
            trLogin1.setType(1);
            boolean save = trLoginService.save(trLogin1);
            if (!save){
                throw new CustomException("err");
            }
            //首次使用该第三方,返回msg，让前端让弹出表单进行绑定或者新建
            UserResult userResult = new UserResult();
            userResult.setId(trID);
            return R.successOnlyObjectWithStatus(userResult, Code.SUCCESSWITHQ);
        } else if (trLogin.getStatus().equals(1)){
            //已经绑定或者注册了,直接登录
            Long userId = trLogin.getUserId();
            if (userId==null){
                return R.error("登录失败");
            }
            String uuid = RandomName.getUUID();//uuid作为key
            User one = userService.getById(userId);
            String token = JWTUtil.getToken(String.valueOf(one.getId()),String.valueOf(one.getPermission()),uuid);
            iStringRedisService.setTokenWithTime(uuid, String.valueOf(one.getId()),3600L);//token作为value，id是不允许更改的
            UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),String.valueOf(one.getStudentId()),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getPermission(),token,one.getEmail(),one.getAvatar());
            return R.success(UserResult);
        } else if (trLogin.getStatus().equals(0)) {
            //首次使用该第三方,返回msg，让前端让弹出表单进行绑定或者新建
            UserResult userResult = new UserResult();
            userResult.setId(trID);
            return R.successOnlyObjectWithStatus(userResult, Code.SUCCESSWITHQ);
        }

        //未注册可选择绑定或者是注册
        return null;
    }

    @Transactional
    @Override
    public R<UserResult> firstEN(Long trId, Integer type, String username, String password) {
        if (type==null){
            throw new CustomException("业务异常");
        }
        if (trId==null){
            throw new CustomException("业务异常");
        }
        if (StringUtils.isEmpty(username)){
            throw new CustomException("业务异常");
        }
        if (StringUtils.isEmpty(password)){
            throw new CustomException("业务异常");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User one = userService.getOne(userLambdaQueryWrapper);
        if (one==null){
            throw new CustomException("服务异常");
        }
        String md5Encryption = PWDMD5.getMD5Encryption(password, one.getSalt());
        System.out.println(md5Encryption);
        System.out.println(one.getPassword());

        if (type.equals(1)){
            TrLogin trLogin = new TrLogin();
            if (md5Encryption.equals(one.getPassword())){

                trLogin.setTrId(trId);
                trLogin.setStatus(1);
                trLogin.setUserId(one.getId());
                trLogin.setTrId(trId);
                trLogin.setType(1);

            }else {
                return R.error("密码错了");
            }
            //绑定原有账号
            LambdaUpdateWrapper<TrLogin> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(TrLogin::getTrId,trId);
            lambdaUpdateWrapper.set(TrLogin::getUserId,one.getId());
            lambdaUpdateWrapper.set(TrLogin::getStatus,1);
            boolean update = trLoginService.update(lambdaUpdateWrapper);
            if (!update){
                throw new CustomException("服务异常");
            }
            Long userId = trLogin.getUserId();
            if (userId==null){
                return R.error("登录失败");
            }
            String uuid = RandomName.getUUID();//uuid作为key
            User ones = userService.getById(userId);
            String token = JWTUtil.getToken(String.valueOf(ones.getId()),String.valueOf(ones.getPermission()),uuid);
            iStringRedisService.setTokenWithTime(uuid, String.valueOf(ones.getId()),3600L);//token作为value，id是不允许更改的
            UserResult UserResult = new UserResult(String.valueOf(ones.getId()),ones.getUsername(),ones.getName(),ones.getPhone(),ones.getSex(),String.valueOf(ones.getStudentId()),ones.getStatus(),ones.getCreateTime(),ones.getUpdateTime(),ones.getPermission(),token,ones.getEmail(),ones.getAvatar());
            return R.success(UserResult);
        }else if (type.equals(2)){
            if (!password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$")){
                return R.error("密码必须字母加数字,8-16位");
            }
            String salts = PWDMD5.getSalt();
            //给密码加盐加密
            String createMD5Encryption = PWDMD5.getMD5Encryption(password,salts);
            User user = new User();
            user.setSalt(salts);
            user.setPassword(createMD5Encryption);
            user.setStatus(1);
            user.setUsername(username);
            //和注册一样，只能是用户权限
            user.setPermission(2);
            user.setName(RandomName.getUUID());
            user.setAvatar("未知");
            boolean save = userService.save(user);
            if (!save){
                throw new CustomException("出现了问题");
            }
            LambdaUpdateWrapper<TrLogin> trLoginEnLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            trLoginEnLambdaUpdateWrapper.eq(TrLogin::getTrId,trId);
            trLoginEnLambdaUpdateWrapper.set(TrLogin::getUserId,user.getId());
            trLoginEnLambdaUpdateWrapper.set(TrLogin::getStatus,1);
            boolean update = trLoginService.update(trLoginEnLambdaUpdateWrapper);
            if (!update){
                throw new CustomException("trLoginEnService:err");
            }
            String uuid = RandomName.getUUID();//uuid作为key
            User ones = userService.getById(user.getId());
            String token = JWTUtil.getToken(String.valueOf(ones.getId()),String.valueOf(ones.getPermission()),uuid);
            iStringRedisService.setTokenWithTime(uuid, String.valueOf(ones.getId()),3600L);//token作为value，id是不允许更改的
            UserResult UserResult = new UserResult(String.valueOf(ones.getId()),ones.getUsername(),ones.getName(),ones.getPhone(),ones.getSex(),String.valueOf(ones.getStudentId()),ones.getStatus(),ones.getCreateTime(),ones.getUpdateTime(),ones.getPermission(),token,ones.getEmail(),ones.getAvatar());
            return R.success(UserResult);
        }
        return null;
    }
}
