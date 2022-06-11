package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;

public interface SpecificationService<T> {
    /**
     * 根据请求参数查询规格
     *
     * @param request
     * @return
     */
    PageResponse<T> querySpecificationPage(PageRequest request);

//    ResultMessage addOne(Brand brand);
//
//    ResultMessage updateOne(Brand brand);
//
//    ResultMessage deleteByIds(Long[] ids);
}
