package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class BrandServiceImpl implements BrandService {
    @Override
    public String findAll() {
        return "远程调用 : findAll方法";
    }
}
