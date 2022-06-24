package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.good.BrandDao;
import com.myjava.core.dao.specification.SpecificationOptionDao;
import com.myjava.core.dao.template.TypeTemplateDao;
import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.response.TemplateResponse;
import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.pojo.specification.SpecificationOption;
import com.myjava.core.pojo.specification.SpecificationOptionQuery;
import com.myjava.core.pojo.template.TypeTemplate;
import com.myjava.core.pojo.template.TypeTemplateQuery;
import com.myjava.core.pojo.template.TypeTemplateSearch;
import com.myjava.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TypeTemplateDao dao;

    @Autowired
    SpecificationOptionDao specOptionDao;

    @Autowired
    BrandDao brandDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PageResponse<TypeTemplate> getPage(PageRequest<String> request) {
        //在这里缓存模板 对应的品牌 和 规格
        if (!redisTemplate.hasKey(Constants.TEMPLATE_LIST_REDIS_KEY)) {
            //查询出所有的模板
            List<TypeTemplate> allTemplates = dao.selectByExample(null);
            for (TypeTemplate template : allTemplates) {
                // key : 模板id
                Long key = template.getId();
                TemplateResponse templateResponse = this.getSpecById(key);
                // 要保存当返回对象中的 规格以及规格选项 对象
                List<SpecificationResponse> specificationList = templateResponse.getSpecificationList();
                // 获取到的品牌信息
                List<Brand> brandList = this.getBrandsByTempId(template.getId());
                // 新建对象保存 品牌 规格 规格选项
                TypeTemplateSearch value = new TypeTemplateSearch();
                value.setId(key);
                value.setSpecificationList(specificationList);
                value.setBrandList(brandList);
                redisTemplate.boundHashOps(Constants.TEMPLATE_LIST_REDIS_KEY).put(key, value);
            }
        }
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

    @Override
    public List<Brand> getBrandsByTempId(Long id) {
        TypeTemplate template = dao.selectByPrimaryKey(id);
        if (template == null) {
            return null;
        }
        String brandIds = template.getBrandIds();
        List<Brand> brands = JSON.parseObject(brandIds, new TypeReference<List<Brand>>() {
        });
        return brands;
    }

}
