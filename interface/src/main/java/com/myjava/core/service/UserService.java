package com.myjava.core.service;

import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.user.User;

public interface UserService {
    void sendCode(String phone);

    ResultMessage addUser(String smscode, User user);
}
