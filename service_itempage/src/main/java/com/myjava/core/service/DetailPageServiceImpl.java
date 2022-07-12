package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.Enum.ItemStatus;
import com.myjava.core.pojo.item.Item;
import com.myjava.core.pojo.item.ItemQuery;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.util.RedisBloomUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DetailPageServiceImpl implements DetailPageService {
    @Autowired
    ItemDao itemDao;
    @Autowired
    RedisBloomUtil redisBloomUtil;

    private void initBloomFilter() {
        redisBloomUtil.createBloomFilter();
        Set<String> bloomList = new HashSet<>();
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        //查询出所有正常的商品
        criteria.andStatusEqualTo(ItemStatus.NORMAL.getCode());
        List<Item> all = itemDao.selectByExample(query);
        for (Item item : all) {
            bloomList.add(String.valueOf(item.getGoodsId()));
        }
        //将查询出的商品id存到布隆过滤器中
        redisBloomUtil.insertDataIntoBloom(bloomList);
    }

    @Override
    public ResultMessage hasDetailPage(Long id) {
        if (redisBloomUtil.bloomFilter == null) {
            initBloomFilter();
        }
        boolean b = redisBloomUtil.containsObj(String.valueOf(id));
        if (b) {
            return new ResultMessage(b, "存在详情页");
        } else {
            return new ResultMessage(b, "不存在该商品");
        }
    }
}
