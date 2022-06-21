package com.myjava.core.service;

import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.response.PageResponse;

import java.util.List;

public interface CategoryService {
    PageResponse findPage(ContentCategory category, Integer current, Integer pageSize);

    void add(ContentCategory category);

    void update(ContentCategory category);

    void delete(Long[] ids);

    List<ContentCategory> getAll();
}
