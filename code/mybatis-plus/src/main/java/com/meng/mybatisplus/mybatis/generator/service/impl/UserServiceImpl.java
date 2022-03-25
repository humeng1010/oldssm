package com.meng.mybatisplus.mybatis.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meng.mybatisplus.mybatis.generator.mapper.UserMapper;
import com.meng.mybatisplus.mybatis.generator.pojo.User;
import com.meng.mybatisplus.mybatis.generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author humeng
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-03-24 20:16:11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public int insertSelective(User user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public int deleteByIdAndName(Long id, String name) {
        return userMapper.deleteByIdAndName(id, name);
    }

    @Override
    public int updateNameAndAgeById(String name, Integer age, Long id) {
        return userMapper.updateNameAndAgeById(name, age, id);
    }

    @Override
    public int updateDeletedById(Integer deleted, Long id) {
        return userMapper.updateDeletedById(deleted, id);
    }
}




