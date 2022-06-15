package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.service.ItemCateService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCate")
public class ItemCategoryController {
    @Reference
    ItemCateService service;

    @RequestMapping("/findByParentId/{id}")
    public List<ItemCat> findByParentId(@PathVariable Long id) {
        return service.findByParentId(id);
    }
}
