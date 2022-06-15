package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.SellerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Reference
    SellerService service;

    @RequestMapping("/getLoginDetail")
    public Seller getDetail() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getOneByName(name);
    }
}
