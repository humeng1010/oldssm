package com.meng.mybatisplus.mybatis.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meng.mybatisplus.mybatis.generator.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author humeng
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2022-03-24 20:16:11
 * @Entity com.meng.mybatis.generator.pojo.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 选择性的添加
     *
     * @param user
     * @return
     */
    int insertSelective(User user);

    int deleteByIdAndName(@Param("id") Long id, @Param("name") String name);

    int updateNameAndAgeById(@Param("name") String name, @Param("age") Integer age, @Param("id") Long id);

    int updateDeletedById(@Param("deleted") Integer deleted, @Param("id") Long id);

}




