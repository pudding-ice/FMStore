package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    SellerService service;

    @RequestMapping("/save")
    public ResultMessage save(@RequestBody Seller enterprise) {
        return service.save(enterprise);
    }
}
