package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.seller.SellerDao;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.pojo.seller.SellerQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Override
    public PageResponse<Seller> getPage(PageRequest<SellerQueryContent> request) {
        SellerQuery query = new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
        ;
        //只有没有通过审核的才需要展示
        criteria.andStatusEqualTo("0");
        SellerQueryContent queryContent = request.getQueryContent();
        if (queryContent != null) {
            String name = queryContent.getName();
            if (name != null && !"".equals(name)) {
                criteria.andNameLike("%" + name + "%");
            }
            String nickName = queryContent.getNickName();
            if (nickName != null && !"".equals(nickName)) {
                criteria.andNickNameLike("%" + nickName + "%");
            }
        }

        try {
            Integer current = request.getCurrent();
            Integer pageSize = request.getPageSize();
            PageHelper.startPage(current, pageSize);
            List<Seller> sellers = dao.selectByExample(query);
            PageInfo<Seller> info = new PageInfo<>(sellers, current);
            long total = info.getTotal();
            PageResponse response = new PageResponse();
            response.setRows(sellers);
            response.setTotal(total);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultMessage updateStatus(String id, String status) {
        Seller seller = new Seller();
        seller.setSellerId(id);
        seller.setStatus(status);
        try {
            //由于上面是新建的一个对象很多字段都是null,如果使用 dao.selectByPrimaryKey() 就会导致这一行除了id和status,
            //其他字段都被设置为null,updateByPrimaryKeySelective方法会使用动态SQL决定更新什么
            dao.updateByPrimaryKeySelective(seller);
            return new ResultMessage(true, "更新状态成功");
        } catch (Exception e) {
            return new ResultMessage(false, "更新状态失败");
        }
    }
}
