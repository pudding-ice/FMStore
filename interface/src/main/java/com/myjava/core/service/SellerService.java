package com.myjava.core.service;

import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.request.SellerQueryContent;
import com.myjava.core.pojo.response.PageResponse;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;

public interface SellerService {
    ResultMessage save(Seller seller);

    PageResponse<Seller> getPage(PageRequest<SellerQueryContent> request);
}
