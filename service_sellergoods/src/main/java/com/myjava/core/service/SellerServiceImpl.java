package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.dao.seller.SellerDao;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao dao;

    @Override
    public ResultMessage save(Seller seller) {
        try {
            dao.insertSelective(seller);
            return new ResultMessage(true, "保存成功");
        } catch (Exception e) {
            return new ResultMessage(false, "保存失败");
        }
    }
}
