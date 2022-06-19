package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.BrandQueryContent;
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

    @RequestMapping("/getPage/{current}/{pageSize}")
    public PageResponse<Brand> getPage(@PathVariable Integer current, @PathVariable Integer pageSize, @RequestBody BrandQueryContent queryContent) {
        PageRequest<BrandQueryContent> request = new PageRequest(current, pageSize, queryContent);
        PageResponse<Brand> response = service.queryPage(request);
        return response;
    }

    @PostMapping("/save")
    public ResultMessage save(@RequestBody Brand brand) {
        if (brand.getId() == null) {
            try {
                service.addOne(brand);
                return new ResultMessage(true, "添加品牌成功");
            } catch (Exception e) {
                return new ResultMessage(false, "添加品牌失败");
            }
        } else {
            try {
                service.updateOne(brand);
                return new ResultMessage(true, "更新成功");
            } catch (Exception e) {
                return new ResultMessage(false, "更新失败");
            }
        }
    }

    @PostMapping("/delete")
    public ResultMessage deleteSelected(Long[] ids) {
        try {
            service.deleteByIds(ids);
            return new ResultMessage(true, "删除成功");
        } catch (Exception e) {
            return new ResultMessage(false, "删除失败");
        }
    }

    @GetMapping("/selectOptionList")
    public List<Brand> getAllBrands() {
        return service.getAll();
    }
}
