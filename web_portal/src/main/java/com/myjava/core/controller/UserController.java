package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.user.User;
import com.myjava.core.service.UserService;
import com.myjava.core.utils.PhoneFormatCheckUtils;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("/getUserInfo")
    public User getUserInfo() {
//        org.springframework.security.core.userdetails.User user =  (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
