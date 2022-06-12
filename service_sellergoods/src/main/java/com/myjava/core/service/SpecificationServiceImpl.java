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
    public ResultMessage addOne(SpecificationRequest request) {
        try {
            Specification specification = request.getSpec();
            specDao.insertSelective(specification);
            List<SpecificationOption> options = request.getSpecOpts();
            for (SpecificationOption option : options) {
                option.setSpecId(specification.getId());
                specOptionDao.insertSelective(option);
            }
            return new ResultMessage(true, "添加规格成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "添加规格失败");
        }
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
    public ResultMessage updateOne(SpecificationRequest request) {
        try {
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
            specDao.updateByPrimaryKeySelective(request.getSpec());
            return new ResultMessage(true, "更新成功");
        } catch (Exception e) {
            return new ResultMessage(false, "更新失败");
        }
    }


    @Override
    public ResultMessage deleteByIds(Long[] ids) {
        try {
            List<Long> list = Arrays.asList(ids);
            for (Long specId : list) {
                //先根据specId删除它所关联的options
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = query.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                specOptionDao.deleteByExample(query);
                //再根据id删除规格
                specDao.deleteByPrimaryKey(specId);
            }
            return new ResultMessage(true, "删除成功");
        } catch (Exception e) {
            return new ResultMessage(false, "删除失败");
        }
    }

    @Override
    public List<Specification> getAll() {
        List<Specification> specifications = specDao.selectByExample(null);
        return specifications;
    }
}
