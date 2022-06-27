package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.List;

@Service
public class SolrManagerServiceImpl implements SolrManagerService {
    @Autowired
    SolrTemplate solrTemplate;

    @Override
    public void saveItemToSolr(List items) {
        try {
            if (items != null) {
                solrTemplate.saveBeans(items);
                solrTemplate.commit();
                System.out.println("数据导入solr成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据导入solr失败");
        }
    }

    @Override
    public void deleteItemByGoodsId(List goodsIds) {
        try {
            if (goodsIds != null) {
                SimpleQuery query = new SimpleQuery();
                Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
                query.addCriteria(criteria);
                solrTemplate.delete(query);
                solrTemplate.commit();
                System.out.println("删除item在solr的索引成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除item在solr的索引失败");
        }
    }
}
