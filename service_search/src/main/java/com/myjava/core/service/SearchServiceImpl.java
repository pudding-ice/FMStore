package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    SolrTemplate solrTemplate;
    @Autowired
    ItemDao dao;

    @Override
    public Map<String, Object> search(Map paramMap) {
        //获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
        //当前页
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        //每页查询多少条数据
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
        //创建查询对象
        Query query = new SimpleQuery();
        //创建查询条件对象
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        //将查询条件放入到查询对象当中
        query.addCriteria(criteria);
        //计算从第几条开始查
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        Integer start = (pageNo - 1) * pageSize;
        //设置从第几条开始查询
        query.setOffset(start);
        //设置每页查询多少条
        query.setRows(pageSize);
        //查询,并返回结果
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        Map<String, Object> resultMap = new HashMap<>();
        //查询到的结果集
        resultMap.put("rows", items.getContent());
        //查询到的总页数
        resultMap.put("totalPages", items.getTotalPages());
        //查询到的总条数
        resultMap.put("total", items.getTotalElements());
        return resultMap;
    }
}
