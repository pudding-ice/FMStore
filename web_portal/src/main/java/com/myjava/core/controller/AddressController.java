package com.myjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.address.Address;
import com.myjava.core.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;

    @RequestMapping("/findListByLoginUser")
    public List<Address> findListByLoginUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Address> addressList = addressService.findListByLoginUser(userName);
        return addressList;
    }
}
