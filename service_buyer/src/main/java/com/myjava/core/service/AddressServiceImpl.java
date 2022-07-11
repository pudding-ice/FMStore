package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.address.AddressDao;
import com.myjava.core.pojo.address.Address;
import com.myjava.core.pojo.address.AddressQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findListByLoginUser(String userName) {
        AddressQuery query = new AddressQuery();
        AddressQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(userName);
        return addressDao.selectByExample(query);
    }
}
