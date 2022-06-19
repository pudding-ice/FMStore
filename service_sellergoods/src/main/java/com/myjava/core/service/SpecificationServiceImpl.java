package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.specification.SpecificationDao;
import com.myjava.core.dao.specification.SpecificationOptionDao;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SpecificationRequest;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.response.SpecificationResponse;
import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.pojo.specification.SpecificationOption;
import com.myjava.core.pojo.specification.SpecificationOptionQuery;
import com.myjava.core.pojo.specification.SpecificationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class SpecificationServiceImpl<T> implements SpecificationService {
    @Autowired
    private SpecificationDao specDao;
    @Autowired
    private SpecificationOptionDao specOptionDao;

    @Override
    public PageResponse<Specification> querySpecificationPage(PageRequest request) {
        SpecificationQuery query = new SpecificationQuery();
        SpecificationQuery.Criteria criteria = query.createCriteria();
        String queryContent = (String) request.getQueryContent();
        if (queryContent != null && !"".equals(queryContent)) {
            criteria.andSpecNameLike("%" + queryContent + "%");
        }
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Specification> specifications = specDao.selectByExample(query);
        PageInfo<Specification> info = new PageInfo<>(specifications, request.getCurrent());
        PageResponse<Specification> response = new PageResponse<>();
        response.setRows(specifications);
        response.setTotal(info.getTotal());
        return response;
    }

    @Override
    public int addOne(SpecificationRequest request) {
        int res = 0;
        Specification specification = request.getSpec();
        res += specDao.insertSelective(specification);
        List<SpecificationOption> options = request.getSpecOpts();
        for (SpecificationOption option : options) {
            option.setSpecId(specification.getId());
            res += specOptionDao.insertSelective(option);
        }
        return res;
    }

    @Override
    public SpecificationResponse getOptionsById(Long id) {
        SpecificationResponse response = new SpecificationResponse();
        Specification specification = specDao.selectByPrimaryKey(id);
        response.setSpec(specification);
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<SpecificationOption> options = specOptionDao.selectByExample(query);
        response.setSpecOpts(options);
        return response;
    }


    @Override
    public int updateOne(SpecificationRequest request) {
        int res = 0;
        //更新规格选项
        List<SpecificationOption> options = request.getSpecOpts();
        //先根据规格id删除所有规格选项
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(request.getSpec().getId());
        specOptionDao.deleteByExample(query);
        //再插入所有规格选项
        for (SpecificationOption option : options) {
            option.setSpecId(request.getSpec().getId());
            specOptionDao.insertSelective(option);
        }
        //再更新规格名称
        res = specDao.updateByPrimaryKeySelective(request.getSpec());
        return res;

    }


    @Override
    public int deleteByIds(Long[] ids) {
        int res = 0;
        List<Long> list = Arrays.asList(ids);
        for (Long specId : list) {
            //先根据specId删除它所关联的options
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecIdEqualTo(specId);
            res += specOptionDao.deleteByExample(query);
            //再根据id删除规格
            res += specDao.deleteByPrimaryKey(specId);
        }
        return res;

    }

    @Override
    public List<Specification> getAll() {
        List<Specification> specifications = specDao.selectByExample(null);
        return specifications;
    }
}
