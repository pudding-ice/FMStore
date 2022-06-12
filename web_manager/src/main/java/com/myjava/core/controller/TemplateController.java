package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.template.TypeTemplate;
import com.myjava.core.service.TemplateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class TemplateController {
    @Reference
    private TemplateService service;

    /**
     * 模板高级分页查询
     */
    @RequestMapping("/search")
    public PageResponse<TypeTemplate> search(@RequestBody PageRequest<String> request) {
        PageResponse<TypeTemplate> result = service.getPage(request);
        return result;
    }

    @PostMapping("/save")
    public ResultMessage save(@RequestBody TypeTemplate template) {
        return service.save(template);
    }
}