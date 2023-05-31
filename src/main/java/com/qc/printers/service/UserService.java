package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.pojo.vo.LoginRes;
import com.qc.printers.pojo.vo.PasswordR;

public interface UserService extends IService<User> {
//    R<UserResult> login(String code);

//    R<UserResult> loginFirst(User user);

    R<String> createUser(User user,Long userId);

    R<UserResult> logout(String token);

    R<Integer> loginByToken();

    R<String> updateUserStatus(String id,String status, Long userId);

    R<UserResult> updateForUser(User user);

    R<UserResult> updateForUserSelf(User user);


    R<String> updateUser(String userid, String name, String username, String phone, String idNumber, String status, String grouping, String sex, String token);

    R<PageData> getUserList(Integer pageNum, Integer pageSize, String name,Long userId);

    R<String> deleteUsers(String id,Long userId);

    R<String> hasUserName(String username);

    R<String> emailWithUser(String emails, String code, String token);

    R<LoginRes> login(User user);

    R<UserResult> info();

    boolean updateUserInfo(User user);

    Integer userPassword();

    boolean setPassword(PasswordR passwordR);
}
