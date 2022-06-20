package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
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

    @RequestMapping("/add")
    public ResultMessage add(@RequestBody ContentCategory category) {
        try {
            categoryService.add(category);
            return new ResultMessage(true, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "保存失败!");
        }
    }

    @RequestMapping("/update")
    public ResultMessage update(@RequestBody ContentCategory category) {
        try {
            categoryService.update(category);
            return new ResultMessage(true, "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "修改失败!");
        }
    }
}