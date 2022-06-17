package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.template.TypeTemplate;

import java.util.List;

public interface TemplateService {
    PageResponse<TypeTemplate> getPage(PageRequest<String> request);

    ResultMessage save(TypeTemplate template);

    ResultMessage deleteByIds(Long[] ids);

    TypeTemplate getOneById(Long id);

    List<SpecificationResponse> getSpecById(Long id);
}
