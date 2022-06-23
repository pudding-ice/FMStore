package com.myjava.core.service;

import com.myjava.core.pojo.admin.Admin;


public interface AdminService {
    Admin getOneByName(String username);
}
