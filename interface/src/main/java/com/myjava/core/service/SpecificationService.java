package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SpecificationRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.response.SpecificationResponse;

public interface SpecificationService<T> {
    /**
     * 根据请求参数查询规格
     *
     * @param request
     * @return
     */
    PageResponse<T> querySpecificationPage(PageRequest request);

    ResultMessage addOne(SpecificationRequest request);

    SpecificationResponse getOptionsById(Long id);


    ResultMessage updateOne(SpecificationRequest request);

    ResultMessage deleteByIds(Long[] ids);
}
