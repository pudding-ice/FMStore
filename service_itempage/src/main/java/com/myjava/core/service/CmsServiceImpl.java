package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.good.GoodsDao;
import com.myjava.core.dao.good.GoodsDescDao;
import com.myjava.core.dao.item.ItemCatDao;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.good.GoodsDesc;
import com.myjava.core.pojo.item.Item;
import com.myjava.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(timeout = 50000)
public class CmsServiceImpl implements CmsService, ServletContextAware {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao descDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao catDao;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    private ServletContext servletContext;


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }



    @Override
    public void createStaticPage(Long goodsId, Map<String, Object> rootMap) throws Exception {
        Configuration configuration = freeMarkerConfig.getConfiguration();
        Template template = configuration.getTemplate("item.ftl");
        //在web_itempage 的target目录中生成一份静态页面
        String thisPath = this.servletContext.getRealPath("/");
        String targetPath = thisPath.replace("service_itempage", "web_itempage") + goodsId + ".html";
        OutputStreamWriter targetOut = new OutputStreamWriter(Files.newOutputStream(new File(targetPath).toPath()), StandardCharsets.UTF_8);
        template.process(rootMap, targetOut);
        targetOut.close();
    }


    @Override
    public Map<String, Object> findGoodsData(Long goodsId) {
        HashMap<String, Object> goodsDataMap = new HashMap<>();
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        GoodsDesc goodsDesc = descDao.selectByPrimaryKey(goodsId);
        String category1 = catDao.selectByPrimaryKey(goods.getCategory1Id()).getName();
        String category2 = catDao.selectByPrimaryKey(goods.getCategory2Id()).getName();
        String category3 = catDao.selectByPrimaryKey(goods.getCategory3Id()).getName();
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemDao.selectByExample(query);
        goodsDataMap.put("itemCat1", category1);
        goodsDataMap.put("itemCat2", category2);
        goodsDataMap.put("itemCat3", category3);
        goodsDataMap.put("goods", goods);
        goodsDataMap.put("goodsDesc", goodsDesc);
        goodsDataMap.put("itemList", itemList);
        return goodsDataMap;
    }

}
