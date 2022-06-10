package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.good.BrandDao;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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

    @Override
    public PageResponse<Brand> queryPage(PageRequest request) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Brand> brands = dao.selectByExample(null);
        PageInfo<Brand> info = new PageInfo<>(brands, request.getCurrent());
        PageResponse<Brand> response = new PageResponse<>(info.getTotal(), brands);
        return response;
    }
}
