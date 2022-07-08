package com.myjava.core.controller;

import com.myjava.core.pojo.order.BuyerCart;
import com.myjava.core.pojo.order.OrderItem;
import com.myjava.core.pojo.response.ResultMessage;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class BuyerCartController {
    /**
     * 添加商品到购物车
     *
     * @param itemId 商品库存id
     * @param num    购买数量
     * @CrossOrigin注解相当于设置了响应头信息, 是w3c支持的一种跨域解决方案
     * origins属性设置的地址是, 返回响应给静态页面, 静态页面所在服务器地址,
     * 也即是service_page项目地址
     */
    @GetMapping("/addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:8086", allowCredentials = "true")
    public ResultMessage addGoodsToCartList(Long itemId, Integer num) {
        try {
            return new ResultMessage(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "添加失败!");
        }
    }

    @RequestMapping("/findCartList")
    public List<BuyerCart> findCartList() {
        /*模拟数据*/
        List<BuyerCart> cartLists = new ArrayList<>();
        BuyerCart buyerCart = new BuyerCart();
        buyerCart.setSellerId("gaowei");
        buyerCart.setSellerName("高伟");
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setTitle("aaaaa 移动3G 16G");
        orderItem1.setGoodsId(new Long(1234));
        orderItem1.setPrice(new BigDecimal(222.00));
        orderItem1.setPicPath("//img14.360buyimg.com/n1/jfs/t1/58623/13/9207/124868/5d68cc8bE4dd71443/3dc6cf36906368e9.jpg");
        orderItem1.setSellerId("gaowei");
        orderItem1.setNum(2);
        orderItem1.setItemId(new Long(1232133));

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setTitle("aaaaa 移动3G 16G222");
        orderItem2.setGoodsId(new Long(12344));
        orderItem2.setPrice(new BigDecimal(122.00));
        orderItem2.setPicPath("//img14.360buyimg.com/n1/jfs/t1/58623/13/9207/124868/5d68cc8bE4dd71443/3dc6cf36906368e9.jpg");
        orderItem2.setSellerId("gaowei");
        orderItem2.setNum(1);
        orderItem2.setItemId(new Long(2332321));

        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);
        buyerCart.setOrderItemList(orderItemList);

        BuyerCart buyerCart2 = new BuyerCart();
        buyerCart2.setSellerId("gaowei2");
        buyerCart2.setSellerName("FMJava");
        OrderItem orderItem3 = new OrderItem();
        orderItem3.setTitle("aaaaa 移动3G 16G1");
        orderItem3.setGoodsId(new Long(1234));
        orderItem3.setPrice(new BigDecimal(232.00));
        orderItem3.setPicPath("//img14.360buyimg.com/n1/jfs/t1/58623/13/9207/124868/5d68cc8bE4dd71443/3dc6cf36906368e9.jpg");
        orderItem3.setSellerId("gaowei");
        orderItem3.setNum(2);
        orderItem3.setItemId(new Long(45454545));

        ArrayList<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(orderItem3);
        buyerCart2.setOrderItemList(orderItemList2);

        cartLists.add(buyerCart);
        cartLists.add(buyerCart2);
        return cartLists;

    }

}
