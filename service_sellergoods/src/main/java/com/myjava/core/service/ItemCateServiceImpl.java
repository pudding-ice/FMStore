package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.item.ItemCatDao;
import com.myjava.core.dao.template.TypeTemplateDao;
import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.pojo.item.ItemCatQuery;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.template.TypeTemplate;
import com.myjava.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ItemCateServiceImpl implements ItemCateService {
    @Autowired
    ItemCatDao itemCatDao;
    @Autowired
    TypeTemplateDao templateDao;

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
        List<ItemCat> itemCats = itemCatDao.selectByExample(query);
        return itemCats;
    }

    @Override
    public List<ItemCat> getAllCategory() {
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
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

    @Override
    public PageResponse<ItemCat> getPage(PageRequest<Long> request) {
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        Integer current = request.getCurrent();
        Integer pageSize = request.getPageSize();
        Long parentId = request.getQueryContent();
        criteria.andParentIdEqualTo(parentId);
        PageHelper.startPage(current, pageSize);
        List<ItemCat> itemCats = itemCatDao.selectByExample(query);
        PageInfo<ItemCat> info = new PageInfo<>(itemCats, current);
        PageResponse<ItemCat> response = new PageResponse<>();
        response.setTotal(info.getTotal());
        response.setRows(itemCats);
        return response;
    }

    @Override
    public List<TypeTemplate> getAllTemplate() {
        return templateDao.selectByExample(null);
    }
}
