package com.myjava.core.service;

import com.myjava.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCateService {
    List<ItemCat> findByParentId(Long id);
}
