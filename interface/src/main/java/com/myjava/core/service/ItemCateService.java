package com.myjava.core.service;

import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.template.TypeTemplate;

import java.util.List;

public interface ItemCateService {
    List<ItemCat> findByParentId(Long id);

    List<ItemCat> getAllCategory();

    PageResponse<ItemCat> getPage(PageRequest<Long> request);

    List<TypeTemplate> getAllTemplate();
}
