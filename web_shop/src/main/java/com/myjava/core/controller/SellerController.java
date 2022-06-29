package com.myjava.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ServletConfigAware;

import javax.servlet.ServletConfig;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    private
    @Reference
    SellerService service;
    @Autowired
    BCryptPasswordEncoder encoder;


    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @RequestMapping("/add")
    public ResultMessage add(@RequestBody Seller enterprise) {
        try {
            String encode = encoder.encode(enterprise.getPassword());
            enterprise.setPassword(encode);
            service.add(enterprise);
            return new ResultMessage(true, "保存成功");
        } catch (Exception e) {
            return new ResultMessage(false, "保存失败");
        }
    }

    @GetMapping("/changePassword/{oldPassword}/{newPassword}")
    public ResultMessage changePassword(@PathVariable String oldPassword, @PathVariable String newPassword) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            return service.changePassword(name, oldPassword, newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "修改密码失败!");
        }
    }

}
