package com.meng.service.impl;

import com.meng.dao.UserDao;
import com.meng.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * <bean id="userService" class="com.meng.service.impl.UserServiceImpl">
 *
 *     </bean>
 */
@Service("userService")
//@Scope("prototype")
@Scope("singleton")
public class UserServiceImpl implements UserService {
    //普通属性的注入
    @Value("${jdbc.driver}")
    private String driver;

    //<property name="userDao" ref="userDao"></property>
    //@Autowired//按照数据类型从Spring容器中匹配
    //@Qualifier("userDao")//是按照id名称从容器中进行匹配的,但是此处@Qualifier要结合@Autowired一起使用
    @Resource(name = "userDao")//@Resource相当于@Autowired+@Qualifier
    private UserDao userDao;

    @PostConstruct
    public void init(){
        System.out.println("Service对象的初始化方法");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("Service对象的销毁方法");
    }


    @Override
    public void save() {
        userDao.save();
        System.out.println(driver);
    }
}
