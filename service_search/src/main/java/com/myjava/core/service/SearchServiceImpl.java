package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.item.Item;
import com.myjava.core.pojo.template.TypeTemplateSearch;
import com.myjava.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
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
    private SolrTemplate solrTemplate;
    @Autowired
    private ItemDao dao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map paramMap) {
        //根据关键字查询商品集对应的分类集
        List itemCategory = this.getCategory(paramMap);
        //查询并且高亮关键词
        Map<String, Object> response = this.getHighlightQuery(paramMap, itemCategory);
        response.put("categoryList", itemCategory);
        //根据商品分类名字查询对应的 品牌 规格 规格选项
        //先查询请求参数中是否有商品分类信息
        String name = (String) paramMap.get("category");
        if (name == null || "".equals(name)) {
            // 获取当前商品分类集中的第一个
            name = (String) itemCategory.get(0);
        }
        TypeTemplateSearch brandAndSpec = this.getBrandAndSpecWithName(name);
        response.put("brandList", brandAndSpec.getBrandList());
        response.put("specificationList", brandAndSpec.getSpecificationList());
        return response;
    }

    private TypeTemplateSearch getBrandAndSpecWithName(String name) {
        // 声明对象保存根据模板id查询出来的 品牌和规格
        TypeTemplateSearch typeTemplateSearch = null;
        // 根据商品的分类名称来获取模板id
        Long tempId = (Long) redisTemplate.boundHashOps(Constants.ITEM_CATEGORY_LIST_REDIS_KEY).get(name);
        // 查询对应的品牌和规格
        typeTemplateSearch = (TypeTemplateSearch) redisTemplate.
                boundHashOps(Constants.TEMPLATE_LIST_REDIS_KEY).get(tempId);
        return typeTemplateSearch;
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

    private Map<String, Object> getHighlightQuery(Map paramMap, List itemCategory) {
        //获取查询条件
        String keywords = String.valueOf(paramMap.get("keywords"));
        //当前页
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        //每页查询多少条数据
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
        //创建查询对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        //判断三个查询条件,添加对应的筛选过滤器
        String category = (String) paramMap.get("category");
        if (category == null || "".equals(category)) {
            // 获取当前商品分类集中的第一个
            category = (String) itemCategory.get(0);
        }
        //将分类关键词作为参数传入
        SimpleFilterQuery filterQuery_cat = new SimpleFilterQuery();
        Criteria criteria_cat = new Criteria("item_category").is(category);
        filterQuery_cat.addCriteria(criteria_cat);
        query.addFilterQuery(filterQuery_cat);
        //判断品牌筛选条件
        String brand = (String) paramMap.get("brand");
        if (brand != null && !"".equals(brand)) {
            SimpleFilterQuery filterQuery_brand = new SimpleFilterQuery();
            Criteria criteria_brand = new Criteria("item_brand").is(brand);
            filterQuery_brand.addCriteria(criteria_brand);
            query.addFilterQuery(filterQuery_brand);
        }
        //判断规格筛选条件
        JSONObject spec = (JSONObject) paramMap.get("spec");
        if (spec != null && spec.size() > 0) {
            for (Map.Entry<String, Object> entry : spec.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                SimpleFilterQuery filterQuery_spec = new SimpleFilterQuery();
                Criteria criteria_spec = new Criteria("item_spec_" + key).is(value);
                filterQuery_spec.addCriteria(criteria_spec);
                query.addFilterQuery(filterQuery_spec);
            }
        }
        //判断价格筛选条件
        String price = (String) paramMap.get("price");
        if (price != null && !"".equals(price)) {
            String[] split = price.split("-");
            if (!"0".equals(split[0])) {
                SimpleFilterQuery filter = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_price").greaterThanEqual(split[0]);
                filter.addCriteria(criteria);
                query.addFilterQuery(filter);
            }
            if (!"*".equals(split[1])) {
                SimpleFilterQuery filter = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_price").lessThanEqual(split[1]);
                filter.addCriteria(criteria);
                query.addFilterQuery(filter);
            }
        }

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
            List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
            Item item = highlightEntry.getEntity();
            //标题中没有高亮文本的话
            if (highlights.size() != 0) {
                //获取高亮文本
                String highlightText = highlightEntry.getHighlights().get(0).getSnipplets().get(0);
                //将item的标题替换成高亮文本
                //获取item
                item.setTitle(highlightText);
            }
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
