package com.myjava.core.dao.admin;

import com.myjava.core.pojo.admin.Admin;
import com.myjava.core.pojo.admin.AdminQuery;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AdminDao {
    int countByExample(AdminQuery example);

    int deleteByExample(AdminQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminQuery example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminQuery example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminQuery example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}