package com.myjava.core.service;

import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.seller.Seller;

public interface SellerService {
    ResultMessage save(Seller seller);
}
