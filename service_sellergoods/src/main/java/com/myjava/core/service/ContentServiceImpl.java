package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.core.dao.ad.ContentDao;
import com.myjava.core.pojo.Enum.ContentStatus;
import com.myjava.core.pojo.ad.Content;
import com.myjava.core.pojo.ad.ContentQuery;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao dao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResponse<Content> findPage(PageRequest<Content> request) {
        Integer current = request.getCurrent();
        Integer pageSize = request.getPageSize();
        PageHelper.startPage(current, pageSize);
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        Content queryContent = request.getQueryContent();
        if (queryContent != null) {
            if (queryContent.getTitle() != null && !"".equals(queryContent.getTitle())) {
                criteria.andTitleLike("%" + queryContent.getTitle() + "%");
            }
        }
        Page<Content> contentList = (Page<Content>) dao.selectByExample(query);
        return new PageResponse<>(contentList.getTotal(), contentList.getResult());
    }

    @Override
    public void add(Content content) {
        dao.insertSelective(content);
    }

    @Override
    public void update(Content content) {
        Content oldContent = dao.selectByPrimaryKey(content.getId());
        if (oldContent.getCategoryId().equals(content.getCategoryId())) {
            //如果更新的广告没有改变它的类别的话,就只需要删除一个类别的缓存
            redisTemplate.boundHashOps(Constants.CONTENT_REDIS_KEY).delete(oldContent.getCategoryId());
        } else {
            //如果类别改变了,就需要删除两个
            redisTemplate.boundHashOps(Constants.CONTENT_REDIS_KEY).delete(oldContent.getCategoryId());
            redisTemplate.boundHashOps(Constants.CONTENT_REDIS_KEY).delete(content.getCategoryId());
        }
        dao.updateByPrimaryKeySelective(content);
    }


    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                dao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Content> findByCategoryId(Long id) {
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        criteria.andCategoryIdEqualTo(id);
        List<Content> contents = dao.selectByExample(query);
        return contents;
    }

    @Override
    public List<Content> findByCategoryIdFromRedis(Long id) {
        List<Content> redisContent = null;
        //不存在这个key,就设置一个值,返回true,如果存在,返回false
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("LOCK", "111");
        if (lock) {
            //获取到锁后查询缓存中的值
            redisContent = (List<Content>) redisTemplate.boundHashOps(Constants.CONTENT_REDIS_KEY).get(id);
            if (redisContent == null) {
                //redis缓存中没有,从数据库查找一份,放到redis
                redisContent = this.findByCategoryId(id);
                redisTemplate.boundHashOps(Constants.CONTENT_REDIS_KEY).put(id, redisContent);
            }
            //释放锁 delete方法
            redisTemplate.delete("LOCK");
        } else {
            //获取锁失败、每隔1秒再获取
            try {
                Thread.sleep(1000);
                findByCategoryIdFromRedis(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return redisContent;
    }

    @Override
    public void open(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                Content content = new Content();
                content.setId(id);
                content.setStatus(ContentStatus.AVAILABLE.getCode());
                dao.updateByPrimaryKeySelective(content);
            }
        }
    }
}
