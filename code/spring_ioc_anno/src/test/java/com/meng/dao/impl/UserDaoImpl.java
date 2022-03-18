package com.meng.dao.impl;

import com.meng.dao.UserDao;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//    <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl"></bean>
@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("saveRunning。。。");
    }
}
