package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.service.SpecificationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spec")
public class SpecificationController {
    @Reference
    private SpecificationService service;

    /**
     * 查询所有的规格id和名称
     *
     * @param current
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping("/getSpecificationPage/{current}/{pageSize}/{name}")
    public PageResponse<Specification> getPage(@PathVariable Integer current, @PathVariable Integer pageSize, @PathVariable String name) {
        PageRequest<String> request = new PageRequest(current, pageSize, name);
        PageResponse<Specification> response = service.querySpecificationPage(request);
        return response;
    }

//    @PostMapping("/save")
//    public ResultMessage save(@RequestBody Brand brand) {
//        if (brand.getId() == null) {
//            return service.addOne(brand);
//        } else {
//            return service.updateOne(brand);
//        }
//    }
//
//    @PostMapping("/delete")
//    public ResultMessage deleteSelected(Long[] ids) {
//        return service.deleteByIds(ids);
//    }

}
