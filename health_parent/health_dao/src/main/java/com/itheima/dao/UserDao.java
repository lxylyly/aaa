package com.itheima.dao;

import com.itheima.pojo.User;

import java.util.Set;

public interface UserDao {
    User findByUserName(String username);
}
