package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.core.pojo.admin.Admin;
import com.myjava.core.pojo.seller.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;


public class AdminDetailServiceImpl implements UserDetailsService {
    private AdminService service;

    /**
     * 需要set方法,spring才可以把service属性注入进来
     *
     * @param service
     */
    public void setService(AdminService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username != null) {
            Admin admin = service.getOneByName(username);
            //如果有这个管理员
            if (admin != null) {
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return new User(username, admin.getPassword(), authorities);
            }
        }
        return null;
    }
}
