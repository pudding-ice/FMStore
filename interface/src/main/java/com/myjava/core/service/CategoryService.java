package com.myjava.core.service;

import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.response.PageResponse;

public interface CategoryService {
    PageResponse findPage(ContentCategory category, Integer current, Integer pageSize);
}
