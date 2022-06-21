package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.admin.AdminDao;
import com.myjava.core.pojo.admin.Admin;
import com.myjava.core.pojo.admin.AdminQuery;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminDao dao;

    @Override
    public Admin getOneByName(String username) {
        AdminQuery query = new AdminQuery();
        AdminQuery.Criteria criteria = query.createCriteria();
        criteria.andNameEqualTo(username);
        return dao.selectByExample(query).get(0);
    }
}
