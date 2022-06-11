package com.myjava.core.service;

import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;

import java.util.List;

public interface BrandService {
    String findAll();

    List<Brand> getAll();

    PageResponse<Brand> queryPage(PageRequest request);

    ResultMessage addOne(Brand brand);

    ResultMessage updateOne(Brand brand);
}
