package com.myjava.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.SellerService;
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

    @RequestMapping("/save")
    public ResultMessage save(@RequestBody Seller enterprise) {
        return service.save(enterprise);
    }

    @PostMapping("/getPage")
    public PageResponse<Seller> getPage(@RequestBody PageRequest<SellerQueryContent> request) {
        return service.getPage(request);
    }
}
