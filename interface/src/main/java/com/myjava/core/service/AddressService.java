package com.myjava.core.service;

import com.myjava.core.pojo.address.Address;

import java.util.List;

public interface AddressService {
    List<Address> findListByLoginUser(String userName);
}