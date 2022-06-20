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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    GoodsService goodsService;

    @Reference
    ItemCateService itemCateService;

    @PostMapping("/save")
    public ResultMessage saveGoods(@RequestBody GoodsEntity entity) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            entity.getGoods().setSellerId(name);
            goodsService.saveGoods(entity);
            return new ResultMessage(true, "保存商品成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "保存商品失败");
        }
    }

    @PostMapping("/getPage")
    public PageResponse<Goods> getPage(@RequestBody PageRequest request) {
        return goodsService.getPage(request);
    }

    @GetMapping("/getCategory")
    public List<ItemCat> getCategory() {
        return itemCateService.getAllCategory();
    }
}
