package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.item.ItemCatDao;
import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.pojo.item.ItemCatQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ItemCateServiceImpl implements ItemCateService {
    @Autowired
    ItemCatDao dao;

    @Override
    public List<ItemCat> findByParentId(Long id) {
        if (id == null || "".equals(id)) {
            id = 0L;
        }
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<ItemCat> itemCats = dao.selectByExample(query);
        return itemCats;
    }
}
