package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.core.dao.ad.ContentCategoryDao;
import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.ad.ContentCategoryQuery;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ContentCategoryDao categoryDao;


    @Override
    public PageResponse<ContentCategory> findPage(PageRequest<String> request) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        ContentCategoryQuery query = new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = query.createCriteria();
        String name = request.getQueryContent();

        if (name != null && !"".equals(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        Page<ContentCategory> categoryList = (Page<ContentCategory>) categoryDao.selectByExample(query);
        return new PageResponse(categoryList.getTotal(), categoryList.getResult());
    }

    @Override
    public void add(ContentCategory category) {
        categoryDao.insertSelective(category);
    }

    @Override
    public void update(ContentCategory category) {
        categoryDao.updateByPrimaryKeySelective(category);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                categoryDao.deleteByPrimaryKey(id);
            }
        }
    }

}
