package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.good.BrandDao;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.good.BrandQuery;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setOrderByClause("first_char asc");
        List<Brand> brands = dao.selectByExample(brandQuery);
        PageInfo<Brand> info = new PageInfo<>(brands, request.getCurrent());
        PageResponse<Brand> response = new PageResponse<>(info.getTotal(), brands);
        return response;
    }

    @Override
    public ResultMessage addOne(Brand brand) {
        try {
            dao.insertSelective(brand);
            return new ResultMessage(true, "保存品牌成功");
        } catch (Exception e) {
            return new ResultMessage(false, "保存品牌失败");
        }
    }

    @Override
    public ResultMessage updateOne(Brand brand) {
        try {
            int i = dao.updateByPrimaryKeySelective(brand);
            return new ResultMessage(true, "更新成功");
        } catch (Exception e) {
            return new ResultMessage(false, "更新失败");
        }
    }

    @Override
    public ResultMessage deleteByIds(Long[] ids) {
        try {
            BrandQuery brandQuery = new BrandQuery();
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
            criteria.andIdIn(Arrays.asList(ids));
            dao.deleteByExample(brandQuery);
            return new ResultMessage(true, "删除成功");
        } catch (Exception e) {
            return new ResultMessage(false, "删除失败");
        }
    }
}
