package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SpecificationRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.service.SpecificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 保存前端的规格,规格选项
     *
     * @param request
     * @return
     */
    @PostMapping("/save")
    public ResultMessage save(@RequestBody SpecificationRequest request) {
        if (request.getSpec().getId() == null) {
            //添加操作
            try {
                service.addOne(request);
                return new ResultMessage(true, "添加规格成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultMessage(false, "添加规格失败");
            }
        } else {
            try {
                service.updateOne(request);
                return new ResultMessage(true, "更新成功");
            } catch (Exception e) {
                return new ResultMessage(false, "更新失败");
            }
        }
    }

    @GetMapping("/getOpsById/{id}")
    public SpecificationResponse getOneById(@PathVariable Long id) {
        return service.getOptionsById(id);
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
    public List<Specification> getAllSpecs() {
        return service.getAll();
    }
}
