package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.service.DetailPageService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/detail")
public class DetailPageController {
    @Reference
    private DetailPageService service;

    @RequestMapping("/getDetailPage/{id}")
    public ResultMessage getDetailPage(@PathVariable Long id) {
        return service.hasDetailPage(id);
    }
}
