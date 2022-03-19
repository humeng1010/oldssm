package com.meng.dao.impl;

import com.meng.dao.UserDao;

public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("saveRunning...");
    }
}
