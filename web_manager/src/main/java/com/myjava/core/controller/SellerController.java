package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    private SellerService service;

    @PostMapping("/getPage")
    public PageResponse<Seller> getPage(@RequestBody PageRequest<SellerQueryContent> request) {
        return service.getPage(request);
    }

    @PostMapping("/getAllPage")
    public PageResponse<Seller> getAllPage(@RequestBody PageRequest<SellerQueryContent> request) {
        return service.getAllPage(request);
    }

    @GetMapping("/updateStatus/{id}/{status}")
    public ResultMessage updateStatus(@PathVariable String id, @PathVariable String status) {
        try {
            service.updateStatus(id, status);
            return new ResultMessage(true, "更新状态成功");
        } catch (Exception e) {
            return new ResultMessage(false, "更新状态失败");
        }
    }

    @PostMapping("/auditAccept")
    public ResultMessage auditAccept(String[] ids) {
        try {
            service.auditAccept(ids);
            return new ResultMessage(true, "审核通过!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "审核失败!");
        }
    }

    @PostMapping("/rejectApply")
    public ResultMessage rejectApply(String[] ids) {
        try {
            service.rejectApply(ids);
            return new ResultMessage(true, "驳回成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "驳回失败!");
        }
    }

    @PostMapping("/closeSeller")
    public ResultMessage closeSeller(String[] ids) {
        try {
            service.closeSeller(ids);
            return new ResultMessage(true, "关闭商家成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "关闭商家失败!");
        }
    }


    @PostMapping("/openSeller")
    public ResultMessage openSeller(String[] ids) {
        try {
            service.openSeller(ids);
            return new ResultMessage(true, "开启成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "开启失败!");
        }
    }

}
