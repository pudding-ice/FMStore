package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.template.TypeTemplateDao;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.template.TypeTemplate;
import com.myjava.core.pojo.template.TypeTemplateQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TypeTemplateDao dao;

    @Override
    public PageResponse<TypeTemplate> getPage(PageRequest<String> request) {
        String content = request.getQueryContent();
        TypeTemplateQuery query = null;
        if (content != null && !"".equals(content)) {
            query = new TypeTemplateQuery();
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            criteria.andNameLike("%" + content + "%");
        }
        Integer pageSize = request.getPageSize();
        Integer current = request.getCurrent();
        PageHelper.startPage(current, pageSize);
        List<TypeTemplate> typeTemplates = dao.selectByExample(query);
        PageInfo<TypeTemplate> info = new PageInfo<>(typeTemplates, current);
        long total = info.getTotal();
        PageResponse<TypeTemplate> response = new PageResponse<>(total, typeTemplates);
        return response;
    }

    @Override
    public ResultMessage save(TypeTemplate template) {
        try {
            dao.insertSelective(template);
            return new ResultMessage(true, "保存成功");
        } catch (Exception e) {
            return new ResultMessage(false, "保存失败");
        }
    }

    @Override
    public ResultMessage deleteByIds(Long[] ids) {
        TypeTemplateQuery query = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        try {
            dao.deleteByExample(query);
            return new ResultMessage(true, "删除成功");
        } catch (Exception e) {
            return new ResultMessage(false, "删除失败");
        }
    }

    @Override
    public TypeTemplate getOneById(Long id) {
        return dao.selectByPrimaryKey(id);
    }
}
