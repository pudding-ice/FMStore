package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.item.Item;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    SolrTemplate solrTemplate;
    @Autowired
    ItemDao dao;

    @Override
    public Map<String, Object> search(Map paramMap) {
        //查询并且高亮
        Map<String, Object> highlightQuery = this.getHighlightQuery(paramMap);
        //根据关键字查询商品集对应的分类集
        List itemCategory = this.getCategory(paramMap);
        highlightQuery.put("categoryList", itemCategory);
        return highlightQuery;
    }

    private List<String> getCategory(Map paramMap) {
        ArrayList<String> result = new ArrayList<>();
        String keywords = String.valueOf(paramMap.get("keywords"));
        SimpleQuery query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        query.addCriteria(criteria);
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);
        //获取结果集中的分类集合
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        //获取分类域的实体集合
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        //遍历获取实体对象
        for (GroupEntry<Item> entry : groupEntries) {
            String groupValue = entry.getGroupValue();
            result.add(groupValue);
        }
        return result;
    }


    private Map<String, Object> getHighlightQuery(Map paramMap) {
        //获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
        //当前页
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        //每页查询多少条数据
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
        //创建查询对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();
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
        //创建高亮选项
        HighlightOptions options = new HighlightOptions();
        //设置哪一个域需要高亮
        options.addField("item_title");
        //设置高亮前缀
        options.setSimplePrefix("<em style=\"color:red\">");
        //设置高亮后缀
        options.setSimplePostfix("</em>");
        //把高亮选项加入到查询对象中
        query.setHighlightOptions(options);
        //查询,并返回结果
        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage(query, Item.class);
        //获取高亮集合
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        //创建一个数组保存新的item对象
        ArrayList<Item> items = new ArrayList<>();
        for (HighlightEntry<Item> highlightEntry : highlighted) {
            //获取高亮文本
            String highlightText = highlightEntry.getHighlights().get(0).getSnipplets().get(0);
            //将item的标题替换成高亮文本
            //获取item
            Item item = highlightEntry.getEntity();
            item.setTitle(highlightText);
            items.add(item);
        }
        Map<String, Object> resultMap = new HashMap<>();
        //查询到的结果集
        resultMap.put("rows", items);
        //查询到的总页数
        resultMap.put("totalPages", highlightPage.getTotalPages());
        //查询到的总条数
        resultMap.put("total", highlightPage.getTotalElements());
        return resultMap;
    }
}
