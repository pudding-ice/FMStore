package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SpecificationRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.specification.Specification;

import java.util.List;

public interface SpecificationService<T> {
    /**
     * 根据请求参数查询规格
     *
     * @param request
     * @return
     */
    PageResponse<T> querySpecificationPage(PageRequest request);

    int addOne(SpecificationRequest request);

    SpecificationResponse getOptionsById(Long id);


    int updateOne(SpecificationRequest request);

    int deleteByIds(Long[] ids);

    List<Specification> getAll();
}
