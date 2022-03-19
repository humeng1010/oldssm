package com.meng.mapper;

import com.meng.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from mybatis.tb_user;")
    List<User> selectAll();
}
