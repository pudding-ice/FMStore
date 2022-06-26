package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.pojo.request.GoodsEntity;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.service.GoodsService;
import com.myjava.core.service.ItemCateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    GoodsService goodsService;

    @Reference
    ItemCateService itemCateService;


    @PostMapping("/getPage")
    public PageResponse<Goods> getPage(@RequestBody PageRequest request) {
        return goodsService.mangerGetPage(request);
    }

    @GetMapping("/getCategory")
    public List<ItemCat> getCategory() {
        return itemCateService.getAllCategory();
    }

    @GetMapping("/getOneById/{id}")
    public GoodsEntity getOneById(@PathVariable Long id) {
        return goodsService.getOneById(id);
    }

    @RequestMapping("/delete")
    public ResultMessage delete(Long[] ids) {
        try {
            //假删除,逻辑删除
            goodsService.delete(ids);
            return new ResultMessage(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "删除失败!");
        }
    }

    @RequestMapping("/updateStatus")
    public ResultMessage updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            return new ResultMessage(true, "状态修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "状态修改失败!");
        }
    }
}
