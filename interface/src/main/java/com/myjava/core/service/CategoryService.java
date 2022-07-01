package com.myjava.core.service;

import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;

import java.util.List;

public interface CategoryService {
    PageResponse findPage(PageRequest<String> request);

    void add(ContentCategory category);

    void update(ContentCategory category);

    void delete(Long[] ids);

    List<ContentCategory> getAll();
}
