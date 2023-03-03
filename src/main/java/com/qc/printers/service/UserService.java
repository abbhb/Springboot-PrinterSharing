package com.qc.printers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.printers.common.R;
import com.qc.printers.pojo.UserResult;
import com.qc.printers.pojo.entity.User;

public interface UserService extends IService<User> {
    R<UserResult> login(String username, String password);

    R<UserResult> logout(String token);

    R<UserResult> loginByToken(String token);

    R<String> updataUserStatus(String userId, String caozuoId, String userStatus, String token);

    R<UserResult> updataForUser(Long valueOf, String username, String name, String sex, String idNumber, String phone, String token);

    R<UserResult> changePassword(String id, String username, String password, String newpassword, String checknewpassword, String token);

    R<String> updataUser(String userid, String name, String username, String phone, String idNumber, String status, String grouping, String sex, String token);
}
