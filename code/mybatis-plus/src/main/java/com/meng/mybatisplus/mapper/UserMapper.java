package com.meng.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meng.mybatisplus.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * 使用了MyBatis之后，只需要在dao层的接口后面让它 继承 BaseMapper!!! 超级简单的，不需要映射sql的xml文件！！
 * 注意需要添加一个约束的泛型,告诉他要去操作什么类型
 * 已经ok了
 */

//@Deprecated//标记本类为过时的
@Repository//表示这个是dao层的注解，就是把这个接口注册到spring容器中交给容器管理
public interface UserMapper extends BaseMapper<User> {
}
