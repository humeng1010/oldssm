package com.meng.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meng.mybatisplus.mapper.UserMapper;
import com.meng.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询name不为空的用户，并且邮箱不为空的数据，年龄大于等于21的
     */
    @Test
    public void selectByCondition(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.isNotNull("name")
                        .isNotNull("email")
                        .ge("age",21);
        List<User> users = userMapper.selectList(userQueryWrapper);
//        System.out.println(users);
        users.forEach(System.out::println);


    }

    /**
     * 查询名字为Tom的
     */
    @Test
    public void test2(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name","Tom");
        User user = userMapper.selectOne(userQueryWrapper);
        System.out.println(user);

    }

    /**
     * 查询年龄在20-30之间的用户
     */
    @Test
    public void test3(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.between("age",20,30);

        Integer integer = userMapper.selectCount(userQueryWrapper);//查询满足这个条件的数量

        System.out.println(integer);

    }

    /**
     * 模糊查询
     */
    @Test
    public void test4(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        userQueryWrapper
                .like("name","T")
                .likeRight("email","test");

        List<Map<String, Object>> maps = userMapper.selectMaps(userQueryWrapper);
//        System.out.println(maps);
        maps.forEach(System.out::println);
    }
    /**
     * 通过ID进行排序
     */
    @Test
    public void test5(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.orderByDesc("id");
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }
}
