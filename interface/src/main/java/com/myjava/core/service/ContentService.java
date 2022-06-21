package com.myjava.core.service;

import com.myjava.core.pojo.ad.Content;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;

public interface ContentService {
    PageResponse<Content> findPage(PageRequest<Content> request);

    void add(Content content);

    void update(Content content);
}

