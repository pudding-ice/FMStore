package com.myjava.core.service;

import com.myjava.core.pojo.request.GoodsEntity;
import org.springframework.stereotype.Service;

@Service
public interface GoodsService {

    void saveGoods(GoodsEntity entity);
}
