package com.myjava.core.pojo.order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BuyerCart implements Serializable {
    //商家ID
    private String sellerId;
    //商家名称
    private String sellerName;
    //购物车明细
    private List<OrderItem> orderItemList;
}