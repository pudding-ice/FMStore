package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.good.BrandDao;
import com.myjava.core.dao.good.GoodsDao;
import com.myjava.core.dao.good.GoodsDescDao;
import com.myjava.core.dao.item.ItemCatDao;
import com.myjava.core.dao.item.ItemDao;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.good.GoodsDesc;
import com.myjava.core.pojo.good.GoodsQuery;
import com.myjava.core.pojo.item.Item;
import com.myjava.core.pojo.item.ItemCat;
import com.myjava.core.pojo.item.ItemQuery;
import com.myjava.core.pojo.request.GoodsEntity;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsDescDao goodsDescDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    ItemCatDao catDao;
    @Autowired
    BrandDao brandDao;

    @Override
    public void addGood(GoodsEntity entity) {
        //先插入商品对象
        Goods goods = entity.getGoods();
        goods.setAuditStatus("0");
        goodsDao.insertSelective(goods);
        //修改dao层xml配置文件,返回生成的id,设置商品描述信息的商品id
        Long goodsId = goods.getId();
        //插入商品描述信息
        GoodsDesc goodsDesc = entity.getGoodsDesc();
        goodsDesc.setGoodsId(goodsId);
        goodsDescDao.insertSelective(goodsDesc);
        //插入商品规格信息
        insertItem(entity);
    }

    @Override
    public void updateGoods(GoodsEntity entity) {
        //1.更新商品
        goodsDao.updateByPrimaryKeySelective(entity.getGoods());
        //2.更新商品详情
        goodsDescDao.updateByPrimaryKeySelective(entity.getGoodsDesc());
        //3.删除商品,再添加商品
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(entity.getGoods().getId());
        itemDao.deleteByExample(query);
        insertItem(entity);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                Goods goods = new Goods();
                goods.setIsDelete("1");
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
            }
        }

    }

    @Override
    public PageResponse<Goods> getPage(PageRequest request) {
        GoodsQuery query = new GoodsQuery();
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        GoodsQuery.Criteria criteria = query.createCriteria();
        criteria.andIsDeleteIsNull();
        List<Goods> goods = goodsDao.selectByExample(query);
        PageInfo<Goods> info = new PageInfo<>(goods, request.getCurrent());
        PageResponse<Goods> response = new PageResponse<>();
        response.setTotal(info.getTotal());
        response.setRows(goods);
        return response;
    }

    @Override
    public GoodsEntity getOneById(Long id) {
        GoodsEntity res = new GoodsEntity();
        Goods goods = goodsDao.selectByPrimaryKey(id);
        res.setGoods(goods);
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        res.setGoodsDesc(goodsDesc);
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(query);
        res.setItemList(items);
        return res;
    }


    public void insertItem(GoodsEntity goodsEntity) {
        if ("1".equals(goodsEntity.getGoods().getIsEnableSpec())) {
            //判断是否有库存规格
            //有勾选规格复选框
            if (goodsEntity.getItemList() != null) {
                for (Item item : goodsEntity.getItemList()) {
                    //库存标题, 由商品名 + 规格组成具体的库存标题, 供消费者搜索使用, 可以搜索的更精确
                    String title = goodsEntity.getGoods().getGoodsName();
                    //从库存对象中获取前端传入的json格式规格字符串, 例如: {"机身内存":"16G","网络":"联通3G"}
                    String specJsonStr = item.getSpec();
                    //将json格式字符串转换成对象
                    Map speMap = JSON.parseObject(specJsonStr, Map.class);
                    //获取map中的value集合
                    Collection<String> values = speMap.values();
                    for (String value : values) {
                        title += " " + value;
                    }
                    //设置标题
                    item.setTitle(title);
                    //设置库存对象的属性值
                    setItemValue(goodsEntity, item);
                    itemDao.insertSelective(item);
                }
            }
        } else {
            //没有勾选复选框, 没有库存数据, 但是我们需要初始化一条, 不然前端有可能报错
            Item item = new Item();
            //价格
            item.setPrice(new BigDecimal("99999999999"));
            //库存量
            item.setNum(0);
            //初始化规格
            item.setSpec("{}");
            //标题
            item.setTitle(goodsEntity.getGoods().getGoodsName());
            //设置库存对象的属性值
            setItemValue(goodsEntity, item);
            itemDao.insertSelective(item);
        }
    }


    private Item setItemValue(GoodsEntity goodsEntity, Item item) {
        //商品id
        item.setGoodsId(goodsEntity.getGoods().getId());
        //创建时间
        item.setCreateTime(new Date());
        //更新时间
        item.setUpdateTime(new Date());
        //库存状态, 默认为0, 未审核
        item.setStatus("0");
        //分类id, 库存使用商品的第三级分类最为库存分类
        item.setCategoryid(goodsEntity.getGoods().getCategory3Id());
        //分类名称
        ItemCat itemCat = catDao.selectByPrimaryKey(goodsEntity.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //品牌名称
        Brand brand = brandDao.selectByPrimaryKey(goodsEntity.getGoods().getBrandId());
        item.setBrand(brand.getName());
        //卖家名称
        String sellerName = goodsEntity.getGoods().getSellerId();
        item.setSeller(sellerName);
        //示例图片
        String itemImages = goodsEntity.getGoodsDesc().getItemImages();
        List<Map> maps = JSON.parseArray(itemImages, Map.class);
        if (maps != null && maps.size() > 0) {
            String url = String.valueOf(maps.get(0).get("url"));
            item.setImage(url);
        }
        //价格转换

        return item;
    }
}
