package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.good.BrandDao;
import com.myjava.core.pojo.good.Brand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao dao;

    @Override
    public String findAll() {
        return "远程调用 : findAll方法";
    }

    @Override
    public List<Brand> getAll() {
        List<Brand> brands = dao.selectByExample(null);
        return brands;
    }
}
