package com.meng.dao.impl;

import com.meng.dao.UserDao;
import com.meng.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UserDaoImpl implements UserDao {
    private List<String> strList;
    private Map<String, User> userMap;
    private Properties properties;
    private String username;
    private int age;

    public UserDaoImpl() {
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }
    //    public UserDaoImpl() {
//        System.out.println("userDaoImpl对象被Spring创建");
//    }
//
//    public void init() {
//        System.out.println("初始化方法");
//    }
//
//    public void destroy() {
//        System.out.println("销毁方法");
//    }

    @Override
    public void save() {
        System.out.println("saveRunning...");
//        System.out.println(this.username + "====" + this.age);
        System.out.println(strList);
        System.out.println(userMap);
        System.out.println(properties);
    }
}
