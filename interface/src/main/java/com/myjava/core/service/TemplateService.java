package com.myjava.core.service;

import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.TemplateResponse;
import com.myjava.core.pojo.template.TypeTemplate;

import java.util.List;

public interface TemplateService {
    PageResponse<TypeTemplate> getPage(PageRequest<String> request);

    int save(TypeTemplate template);

    int deleteByIds(Long[] ids);

    TypeTemplate getOneById(Long id);

    TemplateResponse getSpecById(Long id);

    List<Brand> getBrandsByTempId(Long id);
}
