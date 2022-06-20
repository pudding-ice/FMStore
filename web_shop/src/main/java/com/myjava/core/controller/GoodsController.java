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

    @PostMapping("/add")
    public ResultMessage addGoods(@RequestBody GoodsEntity entity) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            entity.getGoods().setSellerId(name);
            goodsService.addGood(entity);
            return new ResultMessage(true, "添加商品成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "添加商品失败");
        }
    }

    @PostMapping("/update")
    public ResultMessage updateGoods(@RequestBody GoodsEntity entity) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!entity.getGoods().getSellerId().equals(name)) {
                return new ResultMessage(false, "没有权限");
            }
            goodsService.updateGoods(entity);
            return new ResultMessage(true, "更新商品成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "更新商品失败");
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
}
