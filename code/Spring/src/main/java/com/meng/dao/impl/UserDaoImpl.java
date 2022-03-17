package com.meng.dao.impl;

import com.meng.dao.UserDao;

public class UserDaoImpl implements UserDao {
    public UserDaoImpl() {
        System.out.println("userDaoImpl对象被Spring创建");
    }

    @Override
    public void save() {
        System.out.println("saveRunning...");
    }
}
