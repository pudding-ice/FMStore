package com.myjava.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.admin.Admin;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.AdminService;
import com.myjava.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Reference
    AdminService service;
    @Autowired
    BCryptPasswordEncoder encoder;

    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }


}
