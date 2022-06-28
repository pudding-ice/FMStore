package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.service.UserService;
import com.myjava.core.utils.PhoneFormatCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("/sendCode/{phone}")
    public ResultMessage sendCode(@PathVariable String phone) {
        try {
            if (phone == null || "".equals(phone)) {
                return new ResultMessage(false, "手机号不能为空!");
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                return new ResultMessage(false, "手机号格式不正确");
            }
            userService.sendCode(phone);
            return new ResultMessage(true, "发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "发送失败!");
        }
    }
}
