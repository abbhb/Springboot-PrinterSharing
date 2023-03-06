package com.qc.printers.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.printers.common.Code;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.mapper.UserMapper;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.IStringRedisService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.PWDMD5;
import com.qc.printers.utils.RandomName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        String token = JWTUtil.getToken(String.valueOf(one.getId()),String.valueOf(one.getPermission()),uuid);
        iStringRedisService.setTokenWithTime(uuid, String.valueOf(one.getId()),3600L);//token作为value，id是不允许更改的
        UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),one.getIdNumber(),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getPermission(),token);
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

        if (user.getPermission().equals(1)){
            User byId = super.getById(userId);
            if (!byId.getPermission().equals(1)){
                return R.error("权限不足");
            }
        }

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
            iStringRedisService.del(uuid.asString());
            return R.successOnlyMsg("下线成功",Code.DEL_TOKEN);
        }catch (Exception e){
            return R.error(Code.DEL_TOKEN,"登陆过期");
        }
    }

    @Override
    public R<UserResult> loginByToken(String token) {
        DecodedJWT decodedJWT = JWTUtil.deToken(token);
        Claim uuid = decodedJWT.getClaim("uuid");
        String value = iStringRedisService.getValue(uuid.asString());
        if (StringUtils.isEmpty(value)) {
            return R.error("登录过期");
        }
        User one = super.getById(Long.valueOf(value));
        if (one==null){
            return R.error("err");
        }
        UserResult UserResult = new UserResult(String.valueOf(one.getId()),one.getUsername(),one.getName(),one.getPhone(),one.getSex(),one.getIdNumber(),one.getStatus(),one.getCreateTime(),one.getUpdateTime(),one.getPermission(),token);
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
        if (Long.valueOf(id).equals(1L)){
            return R.error("禁止操作admin");
        }
        User byId = super.getById(userId);
        if (!byId.getPermission().equals(1)){
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

    @Override
    public R<UserResult> updataForUser(User user, Long userId) {
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
        }if (user.getIdNumber()==null){
            return R.error("更新失败");
        }
        if (user.getPhone()==null){
            return R.error("更新失败");
        }
        if (userId==null){
            return R.error("更新失败");
        }
        if (user.getIdNumber().length()!=18){
            return R.error("18");
        }
        if (user.getId().equals(1L)){
            return R.error("禁止操作admin");
        }
        User byId = super.getById(userId);
        if (byId==null){
            throw new CustomException("id");
        }
        if (!byId.getPermission().equals(1)){
            throw new CustomException("getPermission");
        }
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId,user.getId());
        lambdaUpdateWrapper.set(User::getName,user.getName());
        lambdaUpdateWrapper.set(User::getIdNumber,user.getIdNumber());
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
           
            UserResult userResult = new UserResult(String.valueOf(user1.getId()),user1.getUsername(),user1.getName(),user1.getPhone(),user1.getSex(),user1.getIdNumber(),user1.getStatus(),user1.getCreateTime(),user1.getUpdateTime(),user1.getPermission(),null);
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
}
