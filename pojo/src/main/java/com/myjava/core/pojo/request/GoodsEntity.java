package com.myjava.core.pojo.request;

import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.good.GoodsDesc;
import com.myjava.core.pojo.item.Item;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsEntity implements Serializable {
    private Goods goods;

    private GoodsDesc goodsDesc;

    private List<Item> itemList;
}
