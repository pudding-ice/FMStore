package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myjava.core.dao.ad.ContentCategoryDao;
import com.myjava.core.pojo.ad.ContentCategory;
import com.myjava.core.pojo.ad.ContentCategoryQuery;
import com.myjava.core.pojo.response.PageResponse;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ContentCategoryDao categoryDao;


    @Override
    public PageResponse<ContentCategory> findPage(ContentCategory category, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        ContentCategoryQuery query = new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = query.createCriteria();
        if (category != null) {
            if (category.getName() != null && !"".equals(category.getName())) {
                criteria.andNameLike("%" + category.getName() + "%");
            }
        }
        Page<ContentCategory> categoryList = (Page<ContentCategory>) categoryDao.selectByExample(query);
        return new PageResponse(categoryList.getTotal(), categoryList.getResult());
    }
}
