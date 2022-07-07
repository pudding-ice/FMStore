package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.pojo.Enum.SellerStatus;
import com.myjava.core.pojo.seller.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class UserDetailServiceImpl implements UserDetailsService {
    private SellerService service;

    /**
     * 需要set方法,spring才可以把service属性注入进来
     *
     * @param service
     */
    public void setService(SellerService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username != null) {
            Seller seller = service.getOneByName(username);
            //如果存在这个商家并且通过审核
            if (seller != null && SellerStatus.ACCEPT.getCode().equals(seller.getStatus())) {
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                return new User(username, seller.getPassword(), authorities);
            }
        }
        return null;
    }
}
