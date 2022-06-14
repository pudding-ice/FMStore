package com.myjava.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    SellerService service;
    @Autowired
    BCryptPasswordEncoder encoder;

    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @RequestMapping("/add")
    public ResultMessage add(@RequestBody Seller enterprise) {
        String encode = encoder.encode(enterprise.getPassword());
        enterprise.setPassword(encode);
        return service.add(enterprise);
    }


}
