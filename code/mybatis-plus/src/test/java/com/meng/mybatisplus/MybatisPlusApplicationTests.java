package com.meng.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meng.mybatisplus.mapper.UserMapper;
import com.meng.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class MybatisPlusApplicationTests {

    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        //这个地方的参数是一个Wrapper，条件构造器，这里我们是查询所有，不需要任何条件，所以为null
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 插入功能
     */
    @Test
    public void testInsert(){
        User user = new User();//会帮我们自动生成id！
        user.setName("xiaohu");
        user.setAge(20);
        user.setEmail("1589029587@qq.com");

        int result = userMapper.insert(user);//结果受影响的行数
        System.out.println(result);
        System.out.println(user.getId());//会输出这个记录的id
        System.out.println(user);
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate(){
        User user = new User();//会帮我们自动生成id！
        //通过条件拼接 动态SQL ！！!
        user.setId(1506866823759511558L);//一号数据
        user.setName("xiaohu");
        user.setAge(19);
        user.setEmail("1589029587@qq.com");
        //注意：虽然说名字是UpdateById，但是需要传入的参数是一个User对象
        int count = userMapper.updateById(user);//受影响的行数
        System.out.println(count);

    }

    /**
     * 测试乐观锁成功
     */
    @Test
    public void testOptimisticLocker(){
        //查询用户信息
        User user = userMapper.selectById(1L);
        //修改用户信息
        user.setName("jack");
        user.setEmail("12345@qq.com");
        //3、执行更新操作
        userMapper.updateById(user);

    }
    /**
     * 测试乐观锁失败：多线程下
     */
    @Test
    public void testOptimisticLocker1(){
        //线程1：查询用户信息
        User user = userMapper.selectById(1L);
        user.setName("jack111");
        user.setEmail("12345@qq.com");

        //模拟另外一个用户执行了插队操作：
        User user2 = userMapper.selectById(1L);
        user2.setName("jack2223");
        user2.setEmail("12345@qq.com");

        userMapper.updateById(user2);

        //线程一执行更新操作（自旋锁来多次尝试提交！！！JUC ）
        userMapper.updateById(user);//如果没有乐观锁，就会覆盖插队线程的值
        //最终结果：线程一因为version版本号不同，导致修改失败。结果为jack222

    }

    /**
     * 查询操作
     */
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1506866823759511555L);
        System.out.println(user);
    }

    /**
     * 测试批量查询
     */
    @Test
    public void testSelectBatchId(){
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        List<User> users = userMapper.selectBatchIds(list);
//        System.out.println(users);

        List<User> users1 = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        //users1.forEach(System.out::println);
        System.out.println(users1);
    }

    /**
     * 条件查询 map
     */
    @Test
    public void testSelectBatchIds(){

        Map<String,Object> map = new HashMap<>();
        map.put("name","xiaohu");//需要查询的条件的KV值
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);

    }
    /**
     * 测试分页查询
     */
    @Test
    public void testPage(){
        //参数一：当前页
        //参数二：页面大小
        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page,null);
        List<User> users = page.getRecords();
        System.out.println(users);

        long total = page.getTotal();
        System.out.println(total);//总数据

    }

    /**
     * 删除
     */
    //根据ID删除
    @Test
    public void testDeleteById(){
        userMapper.deleteById(1506866823759511555L);//返回受影响的行数

    }
    //批量删除
    @Test
    public void testDeleteBatchIds(){
        int i = userMapper.deleteBatchIds(Arrays.asList(1506866823759511557L, 1506866823759511558L));
        System.out.println(i);
    }
    //条件删除 map
    @Test
    public void testDeleteByMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","Billie");
        userMapper.deleteByMap(map);
    }



}
