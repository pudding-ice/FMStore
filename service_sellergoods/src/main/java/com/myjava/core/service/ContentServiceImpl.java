package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.core.dao.ad.ContentDao;
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
    private RedisTemplate template;

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
        criteria.andCategoryIdNotEqualTo(id);
        List<Content> contents = dao.selectByExample(query);
        return contents;
    }

    @Override
    public List<Content> findByCategoryIdFromRedis(Long id) {
        List<Content> redisContent = (List<Content>) template.boundHashOps(Constants.CONTENT_REDIS_KEY).get(id);
        if (redisContent == null) {
            //redis缓存中没有,从数据库查找一份,放到redis
            redisContent = this.findByCategoryId(id);
            template.boundHashOps(Constants.CONTENT_REDIS_KEY).put(id, redisContent);
        }
        return redisContent;
    }
}
