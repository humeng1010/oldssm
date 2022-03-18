package com.meng.factory;

import com.meng.dao.UserDao;
import com.meng.dao.impl.UserDaoImpl;

public class StaticFactory {
    public static UserDao getUserDao() {
        return new UserDaoImpl();
    }
}
