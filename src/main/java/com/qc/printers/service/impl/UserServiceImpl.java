package com.qc.printers.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.Code;
import com.qc.printers.common.CustomException;

import com.qc.printers.common.MyString;
import com.qc.printers.common.R;
import com.qc.printers.mapper.UserMapper;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.Permission;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.IRedisService;

import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.PWDMD5;
import com.qc.printers.utils.RandomName;
import com.qc.printers.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.qc.printers.utils.ParamsCalibration.checkSensitiveWords;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final IRedisService iRedisService;

    @Autowired
    public UserServiceImpl(IRedisService iRedisService) {
        this.iRedisService = iRedisService;
    }


    //重写save方法,校验用户名重复
    //不使用唯一索引是为了逻辑删除后避免用户名不能再次使用
    @Transactional
    @Override
    public boolean save(User entity) {
        if (StringUtils.isEmpty(entity.getUsername())){
            throw new CustomException("err:user:save");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,entity.getUsername());
        //会自动加上条件判断没有删除
        int count = super.count(lambdaQueryWrapper);
        if (count>0){
            throw new CustomException("用户名已经存在");
        }
        return super.save(entity);
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
        Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(one.getPermission()));


        String token = JWTUtil.getToken(String.valueOf(one.getId()),String.valueOf(one.getPermission()),uuid);
        iRedisService.setTokenWithTime(uuid, String.valueOf(one.getId()),3600L);//token作为value，id是不允许更改的
        UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),String.valueOf(one.getStudentId()),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getPermission(),permission.getName(),token,one.getEmail(),one.getAvatar());
        return R.success(UserResult);
    }

    @Override
    public R<UserResult> loginByEmail(String email, String password) {
        if (email==null||email.equals("")){
            return R.error("邮箱不存在");
        }
        if (password==null||password.equals("")){
            return R.error("密码不存在");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
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

        Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(one.getPermission()));


        String token = JWTUtil.getToken(String.valueOf(one.getId()),String.valueOf(one.getPermission()),uuid);
        iRedisService.setTokenWithTime(uuid, String.valueOf(one.getId()),3600L);//token作为value，id是不允许更改的
        UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),String.valueOf(one.getStudentId()),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getPermission(),permission.getName(),token,one.getEmail(),one.getAvatar());
        return R.success(UserResult);
    }

    @Transactional
    @Override
    public R<String> createUser(User user,Long userId) {
        if (user.getPermission()==null){
            throw new CustomException("yichang");
        }
        if (StringUtils.isEmpty(user.getName())){
            throw new CustomException("yichang");
        }

        if (StringUtils.isEmpty(user.getUsername())){
            throw new CustomException("yichang");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            throw new CustomException("password");
        }
        if (!StringUtils.isEmpty(user.getEmail())){
            throw new CustomException("参数异常");
        }
        if (user.getUsername().contains("@")){
            throw new CustomException("不可包含'@'");
        }
        if (!user.getPassword().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$")){
            return R.error("密码必须字母加数字,8-16位");
        }

        if (user.getPermission().equals(1)){
            User byId = super.getById(userId);
            if (!byId.getPermission().equals(1)){
                return R.error("权限不足");
            }
        }
        checkSensitiveWords(user.getName());
        String password = user.getPassword();
        String salt = PWDMD5.getSalt();
        String md5Encryption = PWDMD5.getMD5Encryption(password,salt);
        user.setPassword(md5Encryption);
        user.setSalt(salt);
        boolean save = super.save(user);
        if (save){
            return R.success("创建成功");
        }
        throw new CustomException("yichang");
    }

    @Override
    public R<UserResult> logout(String token) {
        if (StringUtils.isEmpty(token)) {
            return R.error(Code.DEL_TOKEN,"登陆过期");
        }
        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim uuid = decodedJWT.getClaim("uuid");
            iRedisService.del(uuid.asString());
            return R.successOnlyMsg("下线成功",Code.DEL_TOKEN);
        }catch (Exception e){
            return R.error(Code.DEL_TOKEN,"登陆过期");
        }
    }

    @Override
    public R<UserResult> loginByToken(String token) {
        DecodedJWT decodedJWT = JWTUtil.deToken(token);
        Claim uuid = decodedJWT.getClaim("uuid");
        String value = iRedisService.getValue(uuid.asString());
        if (StringUtils.isEmpty(value)) {
            return R.error("登录过期");
        }
        User one = super.getById(Long.valueOf(value));
        if (one==null){
            return R.error("err");
        }
        Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(one.getPermission()));

        UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),String.valueOf(one.getStudentId()),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getPermission(),permission.getName(),token,one.getEmail(),one.getAvatar());
        return R.success(UserResult);
    }

    @Override
    public R<String> updataUserStatus(String id,String status, Long userId) {
        if (StringUtils.isEmpty(id)){
            return R.error("无操作对象");
        }
        if (StringUtils.isEmpty(status)){
            return R.error("无操作对象");
        }
        if (userId==null){
            return R.error("无操作对象");
        }

        User byId = super.getById(id);
        if (byId==null){
            //don't hava object
            throw new CustomException("没有对象");
        }
        User myId = super.getById(userId);
        if (myId==null){
            //don't hava object
            throw new CustomException("没有对象");
        }
        if (userId.equals(Long.valueOf(id))){
            return R.error("禁止操作自己账号");
        }
        Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(byId.getPermission()));
        Permission permissionMyId = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(myId.getPermission()));
        if (permissionMyId.getWeight()<=permission.getWeight()){
            return R.error("权限不足");
        }

        List<User> users = new ArrayList<>();
        boolean update = false;
        if (id.contains(",")){
            String[] split = id.split(",");
            for (String s:
                    split) {
                User user = new User();
                user.setId(Long.valueOf(s));
                user.setStatus(Integer.valueOf(status));
                users.add(user);
            }
            update = super.updateBatchById(users);
        }else {
            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(User::getStatus,Integer.valueOf(status));
            lambdaUpdateWrapper.eq(User::getId,Long.valueOf(id));
            update = super.update(lambdaUpdateWrapper);
        }
        if (update){
            return R.success("更改成功");
        }
        return R.error("无操作对象");
    }

    @Transactional
    @Override
    public R<UserResult> updataForUser(User user) {
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

        if (user.getStudentId()>999999999999L){
            return R.error("不能超过12位学号");
        }
        if (user.getId().equals(1L)){
            return R.error("禁止操作admin");
        }

        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId,user.getId());
        lambdaUpdateWrapper.set(User::getName,user.getName());
        lambdaUpdateWrapper.set(User::getStudentId,user.getStudentId());
        lambdaUpdateWrapper.set(User::getUsername,user.getUsername());
        lambdaUpdateWrapper.set(User::getSex,user.getSex());
        lambdaUpdateWrapper.set(User::getPermission,user.getPermission());
        lambdaUpdateWrapper.set(User::getStatus,user.getStatus());
        lambdaUpdateWrapper.set(User::getPhone,user.getPhone());
        boolean update = super.update(lambdaUpdateWrapper);
        if (update){
            return R.success("更新成功");
        }
        return R.error("err");
    }
    @Transactional
    @Override
    public R<UserResult> updataForUserSelf(User user) {
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
        if (user.getStudentId()>999999999999L){
            return R.error("不能超过12位学号");
        }
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId,user.getId());
        lambdaUpdateWrapper.set(User::getName,user.getName());
        lambdaUpdateWrapper.set(User::getStudentId,user.getStudentId());
        lambdaUpdateWrapper.set(User::getUsername,user.getUsername());
        lambdaUpdateWrapper.set(User::getSex,user.getSex());
        lambdaUpdateWrapper.set(User::getPermission,user.getPermission());
        lambdaUpdateWrapper.set(User::getStatus,user.getStatus());
        lambdaUpdateWrapper.set(User::getPhone,user.getPhone());
        boolean update = super.update(lambdaUpdateWrapper);
        if (update){
            return R.success("更新成功");
        }
        return R.error("err");
    }

    @Override
    public R<UserResult> changePassword(String id, String username, String password, String newpassword, String checknewpassword) {
        if (id==null){
            return R.error(Code.DEL_TOKEN,"环境异常,强制下线");
        }
        if (username==null){
            return R.error(Code.DEL_TOKEN,"环境异常,强制下线");
        }
        if (password==null){
            return  R.error("请输入原密码");
        }
        if (newpassword==null){
            return R.error("请输入新密码");
        }
        if (checknewpassword==null){
            return R.error("请输入确认密码");
        }
        if (!newpassword.equals(checknewpassword)){
            return R.error("两次密码不一致!");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,Long.valueOf(id)).eq(User::getUsername,username);
        User one = super.getOne(queryWrapper);
        if (one==null){
            return R.error(Code.DEL_TOKEN,"环境异常,强制下线");
        }
        String salt = one.getSalt();
        if (!PWDMD5.getMD5Encryption(password,salt).equals(one.getPassword())){//前端传入的明文密码加上后端的盐，处理后跟库中密码比对，一样登陆成功
            return R.error("原密码错误");
        }

        String newSalt = PWDMD5.getSalt();
        String newMD5Password = PWDMD5.getMD5Encryption(newpassword,newSalt);
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId,Long.valueOf(id)).eq(User::getUsername,username);
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setUsername(username);
        user.setPassword(newMD5Password);
        user.setSalt(newSalt);
//        employee.setUpdateUser(Long.valueOf(id));
        //操作数据库更新密码和盐
        boolean update = super.update(user, lambdaUpdateWrapper);
        if (update){
            return R.success("修改成功");
        }

        return R.error("修改失败");
    }

    @Override
    public R<String> updataUser(String userid, String name, String username, String phone, String idNumber, String status, String grouping, String sex, String token) {
        return null;
    }

    @Override
    public R<PageData> getUserList(Integer pageNum, Integer pageSize, String name, Long userId) {
        if (pageNum==null){
            return R.error("传参错误");
        }
        if (pageSize==null){
            return R.error("传参错误");
        }
        if (userId==null){
            throw new CustomException("业务异常");
        }
        User byId = super.getById(userId);
        if (byId==null){
            throw new CustomException("业务异常");
        }
        
        
        if (byId.getPermission()==2){
            //当前是User身份,不返回数据
            return R.error("你好像没权限欸!");
        }
        Page pageInfo = new Page(pageNum,pageSize);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),User::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(User::getCreateTime);//按照创建时间排序
        super.page(pageInfo,lambdaQueryWrapper);
        PageData<UserResult> pageData = new PageData<>();
        List<UserResult> results = new ArrayList<>();
        for (Object user : pageInfo.getRecords()) {
            User user1 = (User) user;
            Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(user1.getPermission()));

            UserResult userResult = new UserResult(String.valueOf(user1.getId()),user1.getUsername(),user1.getName(),user1.getPhone(),user1.getSex(),String.valueOf(user1.getStudentId()),user1.getStatus(),user1.getCreateTime(),user1.getUpdateTime(),user1.getPermission(),permission.getName(),null,user1.getEmail(),user1.getAvatar());
            results.add(userResult);
        }
        pageData.setPages(pageInfo.getPages());
        pageData.setTotal(pageInfo.getTotal());
        pageData.setCountId(pageInfo.getCountId());
        pageData.setCurrent(pageInfo.getCurrent());
        pageData.setSize(pageInfo.getSize());
        pageData.setRecords(results);
        pageData.setMaxLimit(pageInfo.getMaxLimit());
        return R.success(pageData);
    }

    @Transactional
    @Override
    public R<String> deleteUsers(String id,Long userId) {
        if (StringUtils.isEmpty(id)){
            return R.error("无操作对象");
        }
        if (userId==null){
            throw new CustomException("环境异常");
        }
        User byId = super.getById(userId);
        if (byId.getPermission().equals(2)){
            //当前是User身份,不返回数据
            return R.error("你好像没权限欸!");
        }
        Collection<Long> ids = new ArrayList<>();
        if (id.contains(",")){
            String[] split = id.split(",");
            for (String s:
                    split) {
                if (s.equals("1")){
                    throw new CustomException("admin不可删除");
                }
                ids.add(Long.valueOf(s));
            }
            super.removeByIds(ids);
        }else {
            if (Long.valueOf(id).equals(1L)){
                throw new CustomException("admin不可删除");
            }
            LambdaQueryWrapper<User> lambdaUpdateWrapper = new LambdaQueryWrapper<>();
            lambdaUpdateWrapper.eq(User::getId,Long.valueOf(id));
            super.remove(lambdaUpdateWrapper);
        }

        return R.success("删除成功");
    }

    @Override
    public R<String> hasUserName(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,username);
        int count = super.count(userLambdaQueryWrapper);
        if (count==0){
            return R.success("可用");
        }
        return R.error("请换一个用户名试试!");

    }

    @Transactional
    @Override
    public R<String> emailWithUser(String emails, String code, String token) {
        if (StringUtils.isEmpty(emails)||StringUtils.isEmpty(code)||StringUtils.isEmpty(token)){
            throw new CustomException("参数异常");
        }

        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim id = decodedJWT.getClaim("id");
            if (!iRedisService.getValue("emailcode:"+id.asString()).equals(code)){
                throw new CustomException("验证码错误");
            }
            LambdaQueryWrapper<User> userLambdaQueryWrapperCount = new LambdaQueryWrapper<>();
            userLambdaQueryWrapperCount.eq(User::getEmail,emails);
            int count = super.count(userLambdaQueryWrapperCount);
            if (count>0){
                throw new CustomException("该账号已经绑定过帐号了!");
            }
            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(User::getEmail,emails);
            lambdaUpdateWrapper.eq(User::getId,Long.valueOf(id.asString()));
            boolean update = super.update(lambdaUpdateWrapper);
            if (update){
                return R.success("绑定成功");
            }
            return R.error("异常");
        }catch (Exception e){
            return R.error(Code.DEL_TOKEN,e.getMessage());
        }
    }

}
