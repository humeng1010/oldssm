package com.meng.factory;

import com.meng.dao.UserDao;
import com.meng.dao.impl.UserDaoImpl;

public class DynamicFactory {
    public UserDao getUserDao() {
        return new UserDaoImpl();
    }
}
