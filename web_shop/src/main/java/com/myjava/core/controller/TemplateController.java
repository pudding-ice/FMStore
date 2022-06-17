package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.template.TypeTemplate;
import com.myjava.core.service.TemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temp")
public class TemplateController {
    @Reference
    private TemplateService service;

    @RequestMapping("/getOne/{id}")
    public TypeTemplate getOneById(@PathVariable Long id) {
        return service.getOneById(id);
    }

    @RequestMapping("/getSpecificationById/{id}")
    public List<SpecificationResponse> getSpecificationById(@PathVariable Long id) {
        return service.getSpecById(id);
    }
}