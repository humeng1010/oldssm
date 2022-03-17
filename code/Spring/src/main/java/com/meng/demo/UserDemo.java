package com.meng.demo;

import com.meng.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDemo {
    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao userDao = app.getBean("userDao", UserDao.class);
        UserDao userDao2 = app.getBean("userDao", UserDao.class);

        userDao.save();
        userDao2.save();

        System.out.println(userDao);
        System.out.println(userDao2);
        System.out.println(userDao == userDao2);
    }
}
