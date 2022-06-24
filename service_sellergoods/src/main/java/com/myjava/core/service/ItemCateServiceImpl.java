package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.item.ItemCatDao;
import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.pojo.item.ItemCatQuery;
import com.myjava.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ItemCateServiceImpl implements ItemCateService {
    @Autowired
    ItemCatDao dao;

    @Autowired
    RedisTemplate redisTemplate;

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

    @Override
    public List<ItemCat> getAllCategory() {
        List<ItemCat> itemCats = dao.selectByExample(null);
        //查询所有商品分类的时候,将其存到redis缓存,在搜索页面使用
        if (!redisTemplate.hasKey(Constants.ITEM_CATEGORY_LIST_REDIS_KEY)) {
            //如果redis缓存中没有商品分类的话就存一份
            //key : name   value : type_id
            for (ItemCat cat : itemCats) {
                String key = cat.getName();
                Long value = cat.getTypeId();
                redisTemplate.boundHashOps(Constants.ITEM_CATEGORY_LIST_REDIS_KEY).put(key, value);
            }
        }
        return itemCats;
    }
}
