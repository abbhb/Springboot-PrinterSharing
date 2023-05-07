package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.Token;
import com.qc.printers.pojo.entity.User;

public interface UserService extends IService<User> {
    R<UserResult> login(String code);

//    R<UserResult> loginFirst(User user);

    R<String> createUser(User user,Long userId);

    R<UserResult> logout(String token);

    R<UserResult> loginByToken(Token token);

    R<String> updataUserStatus(String id,String status, Long userId);

    R<UserResult> updataForUser(User user);

    R<UserResult> updataForUserSelf(User user);


    R<String> updataUser(String userid, String name, String username, String phone, String idNumber, String status, String grouping, String sex, String token);

    R<PageData> getUserList(Integer pageNum, Integer pageSize, String name,Long userId);

    R<String> deleteUsers(String id,Long userId);

    R<String> hasUserName(String username);

    R<String> emailWithUser(String emails, String code, String token);

}
