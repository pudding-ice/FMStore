package com.myjava.core.service;

import com.myjava.core.pojo.order.BuyerCart;

import java.util.List;

public interface CartService {
    List<BuyerCart> addItemToCartList(List<BuyerCart> cartList, Long itemId, Integer num);

    void setCartListToRedis(String userName, List<BuyerCart> buyerCarts);

    List<BuyerCart> getCartListFromRedis(String userName);

    List<BuyerCart> mergeCookieCartListToRedisCartList(List<BuyerCart> cookieCartList, List<BuyerCart> redisCartList);
}
