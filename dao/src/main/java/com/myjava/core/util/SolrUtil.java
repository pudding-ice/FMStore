package com.myjava.core.util;

import com.alibaba.fastjson.JSON;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.item.Item;
import com.myjava.core.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private ItemDao dao;

    public void importItemDataToSolr() {
        try {
            ItemQuery query = new ItemQuery();
            ItemQuery.Criteria criteria = query.createCriteria();
            criteria.andStatusEqualTo("1");
            // 只查找状态是正常的商品
            List<Item> items = dao.selectByExample(query);
            if (items != null) {
                //由于商品的类别信息是json字符串,所以要转换成map集合存储到对象对应的字段中
                for (Item item : items) {
                    String spec = item.getSpec();
                    Map map = JSON.parseObject(spec, Map.class);
                    item.setSpecMap(map);
                }
                solrTemplate.saveBeans(items);
                solrTemplate.commit();
                System.out.println("数据导入solr成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据导入solr失败");
        }
    }
}
