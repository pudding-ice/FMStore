package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.specification.SpecificationOptionDao;
import com.myjava.core.dao.template.TypeTemplateDao;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.response.TemplateResponse;
import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.pojo.specification.SpecificationOption;
import com.myjava.core.pojo.specification.SpecificationOptionQuery;
import com.myjava.core.pojo.template.TypeTemplate;
import com.myjava.core.pojo.template.TypeTemplateQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TypeTemplateDao dao;

    @Autowired
    SpecificationOptionDao specOptionDao;

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
    public int save(TypeTemplate template) {

        return dao.insertSelective(template);

    }

    @Override
    public int deleteByIds(Long[] ids) {
        TypeTemplateQuery query = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));

        return dao.deleteByExample(query);

    }

    @Override
    public TypeTemplate getOneById(Long id) {
        return dao.selectByPrimaryKey(id);
    }

    @Override
    public TemplateResponse getSpecById(Long id) {
        TypeTemplate template = dao.selectByPrimaryKey(id);
        if (template == null) {
            return null;
        }
        TemplateResponse response = new TemplateResponse();
        response.setId(id);
        response.setName(template.getName());

        String templateSpecIds = template.getSpecIds();
        //[{"specName":"选择颜色","id":42},{"specName":"选择版本","id":43},{"specName":"套　　餐","id":44}]
        List<Specification> specifications = JSON.parseArray(templateSpecIds, Specification.class);
        List<SpecificationResponse> specificationResponseList = new ArrayList<>();
        for (Specification spec : specifications) {
            SpecificationResponse specificationResponse = new SpecificationResponse();
            specificationResponse.setSpec(spec);
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecIdEqualTo(spec.getId());
            List<SpecificationOption> options = specOptionDao.selectByExample(query);
            specificationResponse.setSpecOpts(options);
            specificationResponseList.add(specificationResponse);
        }
        response.setSpecificationList(specificationResponseList);
        return response;
    }
}
