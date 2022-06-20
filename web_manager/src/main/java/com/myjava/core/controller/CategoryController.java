package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.service.CategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contentCategory")
public class CategoryController {

    @Reference
    private CategoryService categoryService;

    @RequestMapping("/search")
    public PageResponse search(@RequestBody ContentCategory category, Integer current, Integer pageSize) {
        return categoryService.findPage(category, current, pageSize);
    }
}