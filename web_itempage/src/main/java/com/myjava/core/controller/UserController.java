package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.user.User;
import com.myjava.core.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("/getUserInfo")
    public User getUserInfo() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            username = (String) principal;
        } else {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
            username = user.getUsername();
        }
        User myUser = userService.getUserByName(username);
        return myUser;
    }
}
