package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.good.BrandDao;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.good.BrandQuery;
import com.myjava.core.pojo.request.BrandQueryContent;
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
    public List<Brand> getAll() {
        List<Brand> brands = dao.selectByExample(null);
        return brands;
    }

    @Override
    public PageResponse<Brand> queryPage(PageRequest request) {
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (request.getQueryContent() != null) {
            BrandQueryContent queryContent = (BrandQueryContent) request.getQueryContent();
            brandQuery.setOrderByClause(queryContent.getOrder());
            String name = queryContent.getName();
            if (name != null && !"".equals(name)) {
                criteria.andNameLike("%" + name + "%");
            }
            String firstChar = queryContent.getFirstChar();
            if (firstChar != null && !"".equals(firstChar)) {
                criteria.andFirstCharLike("%" + firstChar + "%");
            }
        }
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Brand> brands = dao.selectByExample(brandQuery);
        PageInfo<Brand> info = new PageInfo<>(brands, request.getCurrent());
        PageResponse<Brand> response = new PageResponse<>(info.getTotal(), brands);
        return response;
    }

    @Override
    public int addOne(Brand brand) {
        return dao.insertSelective(brand);
    }

    @Override
    public int updateOne(Brand brand) {
        return dao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        return dao.deleteByExample(brandQuery);
    }
}
