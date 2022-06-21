package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.core.dao.ad.ContentDao;
import com.myjava.core.pojo.ad.Content;
import com.myjava.core.pojo.ad.ContentQuery;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao contentDao;

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
        Page<Content> contentList = (Page<Content>) contentDao.selectByExample(query);
        return new PageResponse<>(contentList.getTotal(), contentList.getResult());
    }

    @Override
    public void add(Content content) {
        contentDao.insertSelective(content);
    }
}
