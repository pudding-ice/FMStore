package com.myjava.core.service;

import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;

import java.util.List;

public interface BrandService {
    List<Brand> getAll();

    PageResponse<Brand> queryPage(PageRequest request);

    int addOne(Brand brand);

    int updateOne(Brand brand);

    int deleteByIds(Long[] ids);
}
