package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService service;

    @RequestMapping("/getAll")
    public List<Brand> getAll() {
        return service.getAll();
    }

    @GetMapping("/getPage/{current}/{pageSize}")
    public PageResponse<Brand> getPage(@PathVariable Integer current, @PathVariable Integer pageSize) {
        PageRequest request = new PageRequest(current, pageSize);
        PageResponse<Brand> response = service.queryPage(request);
        return response;
    }

    @PostMapping("/save")
    public ResultMessage save(@RequestBody Brand brand) {
        if (brand.getId() == null) {
            return service.addOne(brand);
        } else {
            return service.updateOne(brand);
        }
    }

}
