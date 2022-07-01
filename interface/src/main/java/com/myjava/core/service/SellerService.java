package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;

public interface SellerService {
    int add(Seller seller);

    PageResponse<Seller> getPage(PageRequest<SellerQueryContent> request);

    int updateStatus(String id, String status);

    Seller getOneByName(String username);

    ResultMessage changePassword(String name, String oldPass, String newPass);

    void updateSeller(Seller seller);

    void auditAccept(String[] ids);

    void rejectApply(String[] ids);

    PageResponse<Seller> getAllPage(PageRequest<SellerQueryContent> request);
}
