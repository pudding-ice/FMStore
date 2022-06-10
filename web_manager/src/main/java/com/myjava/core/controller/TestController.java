package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.service.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class TestController {
    @Reference
    private BrandService service;

    @RequestMapping("/getAll")
    public List<Brand> getAll() {
        return service.getAll();
    }
}
