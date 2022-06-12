package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.template.TypeTemplate;

public interface TemplateService {
    PageResponse<TypeTemplate> getPage(PageRequest<String> request);
}
