package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.ad.Content;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    @RequestMapping("/search")
    public PageResponse<Content> search(@RequestBody PageRequest<Content> request) {
        return contentService.findPage(request);
    }

    @RequestMapping("/add")
    public ResultMessage add(@RequestBody Content content) {
        try {
            contentService.add(content);
            return new ResultMessage(true, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "保存失败!");
        }
    }

    @RequestMapping("/update")
    public ResultMessage update(@RequestBody Content content) {
        try {
            contentService.update(content);
            return new ResultMessage(true, "更新成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "更新失败!");
        }
    }

    @RequestMapping("/delete")
    public ResultMessage delete(Long[] ids) {
        try {
            contentService.delete(ids);
            return new ResultMessage(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "删除失败!");
        }
    }
}
