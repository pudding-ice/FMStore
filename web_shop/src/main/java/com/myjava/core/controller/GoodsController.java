package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.request.GoodsEntity;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.service.GoodsService;
import com.myjava.core.service.UserDetailServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    GoodsService service;

    @PostMapping("/save")
    public ResultMessage saveGoods(@RequestBody GoodsEntity entity) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            entity.getGoods().setSellerId(name);
            service.saveGoods(entity);
            return new ResultMessage(true, "保存商品成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "保存商品失败");
        }
    }

    @PostMapping("/getPage")
    public PageResponse<Goods> getPage(@RequestBody PageRequest request) {
        return service.getPage(request);
    }
}
