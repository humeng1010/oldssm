package com.meng.service.impl;

import com.meng.dao.UserDao;
import com.meng.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
    }

    //构造参数注入
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    //    set方法注入
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save() {
        userDao.save();
    }
}
