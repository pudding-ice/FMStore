package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myjava.core.dao.seller.SellerDao;
import com.myjava.core.pojo.Enum.GoodsAuditStatus;
import com.myjava.core.pojo.Enum.ItemStatus;
import com.myjava.core.pojo.Enum.SellerStatus;
import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.item.Item;
import com.myjava.core.pojo.item.ItemQuery;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;
import com.myjava.core.pojo.seller.SellerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao dao;

    @Override
    public int add(Seller seller) {
        return dao.insertSelective(seller);
    }

    @Override
    public PageResponse<Seller> getPage(PageRequest<SellerQueryContent> request) {
        SellerQuery query = new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
        //只有没有通过审核的才需要展示
        criteria.andStatusEqualTo(SellerStatus.NOT_APPLY.getCode());
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
    public PageResponse<Seller> getAllPage(PageRequest<SellerQueryContent> request) {
        SellerQuery query = new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
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
            String status = queryContent.getStatus();
            if (status != null && !"".equals(status) && !"[]".equals(status)) {
                List<String> statusList = JSON.parseObject(status, List.class);
                criteria.andStatusIn(statusList);
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
    public int updateStatus(String id, String status) {
        Seller seller = new Seller();
        seller.setSellerId(id);
        seller.setStatus(status);

        //由于上面是新建的一个对象很多字段都是null,如果使用 dao.selectByPrimaryKey() 就会导致这一行除了id和status,
        //其他字段都被设置为null,updateByPrimaryKeySelective方法会使用动态SQL决定更新什么
        return dao.updateByPrimaryKeySelective(seller);
    }

    @Override
    public Seller getOneByName(String username) {
        Seller seller = dao.selectByPrimaryKey(username);
        return seller;
    }

    @Override
    public ResultMessage changePassword(String name, String oldPass, String newPass) {
        //1.先去数据库中查找用户,看用户输入的旧密码是否正确
        Seller seller = dao.selectByPrimaryKey(name);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(oldPass, seller.getPassword());
        if (!matches) {
            return new ResultMessage(false, "输入旧密码不正确请重新输入!");
        }
        //2.用户输入的旧密码没问题,调用加密工具类加密密码,更新数据库
        seller.setPassword(passwordEncoder.encode(newPass));
        dao.updateByPrimaryKey(seller);
        return new ResultMessage(true, "修改密码成功!");
    }

    @Override
    public void updateSeller(Seller seller) {
        dao.updateByPrimaryKeySelective(seller);
    }

    @Override
    public void auditAccept(String[] ids) {
        if (ids != null) {
            for (String id : ids) {
                //1. 根据商家id修改状态码
                Seller seller = new Seller();
                seller.setSellerId(id);
                seller.setStatus(SellerStatus.ACCEPT.getCode());
                dao.updateByPrimaryKeySelective(seller);
            }
        }
    }

    @Override
    public void rejectApply(String[] ids) {
        if (ids != null) {
            for (String id : ids) {
                Seller seller = new Seller();
                seller.setSellerId(id);
                seller.setStatus(SellerStatus.REJECT.getCode());
                dao.updateByPrimaryKeySelective(seller);
            }
        }
    }


}
