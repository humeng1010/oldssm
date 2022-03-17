# Spring

## Spring简介

### Spring是什么

Spring是分层的Java SE/EE 应用full-stack 轻量级开源框架，以**IoC**（inverse of control：反转控制）和**AOP**（Aspect Oriented Programming：面向切面编程）为内核



提供了**展现层SpringMVC**和**持久层Spring JDBCTemplate**以及**业务层事务管理**等众多的企业级应用技术，还能整合开源世界众多著名的第三方框架和类库，逐渐成为使用最多的Java EE企业应用开源框架



### Spring的优势

1. 方便解耦，简化开发
2. AOP 编程的支持
3. 声明式事务的支持
4. 方便程序的测试
5. 方便集成各种优秀的框架
6. 降低JavaEEAPI的使用难度
7. Java源码是经典学习范例





## Spring快速入门

1. 导入Spring开发的基本坐标
2. 编写Dao接口和实现类
3. 创建Spring核心配置文件
4. 在Spring配置文件中配置UserDaoImpl
5. 使用Spring的API获得Bean实例（getBean）



1. 导入坐标

   ```xml
   <!--        导入Spring对应的依赖坐标-->
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-context</artifactId>
               <version>5.3.16</version>
           </dependency>
   ```

2. Dao接口和实现类

   ```java
   package com.meng.dao;
   
   public interface UserDao {
       void save();
   }
   
   ```

   ```java
   package com.meng.dao.impl;
   
   import com.meng.dao.UserDao;
   
   public class UserDaoImpl implements UserDao {
       @Override
       public void save() {
           System.out.println("saveRunning...");
       }
   }
   
   ```

3. applicationContext.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <!--    把我们的UserDaoImpl实现类交给Spring容器进行管理，交给spring创建对象-->
       <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl"></bean>
   
   </beans>
   ```

4. 使用spring的API（getBean）获取对象

   ```java
   package com.meng.demo;
   
   import com.meng.dao.UserDao;
   import org.springframework.context.ApplicationContext;
   import org.springframework.context.support.ClassPathXmlApplicationContext;
   
   public class UserDemo {
       public static void main(String[] args) {
           ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
           UserDao userDao = app.getBean("userDao", UserDao.class);
           userDao.save();
       }
   }
   
   ```





### Spring的配置文件

#### Bean标签的基本配置

用于配置对象交给Spring来创建

默认情况下它调用的是类中的**无参构造函数**，如果没有无参构造函数则不能创建成功！

基本属性：

- id：Bean实例在Spring容器中的唯一标识
- class：Bean的全限定名称

##### Bean标签的范围配置

scope：指对象的作用范围，取值如下

| 取值范围       | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| **singleton**  | 默认值，单例的                                               |
| **prototype**  | 多例的                                                       |
| request        | WEB项目中，Spring创建一个Bean对象，将对象保存到request域中   |
| session        | WEB项目中，Spring创建一个Bean对象，将对象保存到session域中   |
| global session | WEB项目中，应用在 Portlet 环境，如果没有 Portlet 环境 那么globalSession相当于session |



##### singleton

```xml
    <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl" scope="singleton"></bean>

```

```java
package com.meng.test;

import com.meng.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    @Test
    public void test1() {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");//饿汉模式，当这个配置文件一旦被加载就会创建applicationContext中的对象
        UserDao userDao1 = app.getBean("userDao", UserDao.class);
        UserDao userDao2 = app.getBean("userDao", UserDao.class);

        System.out.println(userDao1);
        System.out.println(userDao2);
        System.out.println(userDao1.equals(userDao2));

    }

}
/*
默认值，单例（饿汉），通过getBean创建的对象是同一个对象

userDaoImpl对象被Spring创建
com.meng.dao.impl.UserDaoImpl@27f723
com.meng.dao.impl.UserDaoImpl@27f723
true
*/
```

##### prototype

```xml
    <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl" scope="prototype"></bean>

```

```java
package com.meng.test;

import com.meng.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    @Test
    public void test1() {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");//懒汉模式，加载配置文件的时候不创建对象，当我们getBean的时候再创建对象
        UserDao userDao1 = app.getBean("userDao", UserDao.class);//创建对象：userDaoImpl对象被Spring创建
        UserDao userDao2 = app.getBean("userDao", UserDao.class);//创建对象：userDaoImpl对象被Spring创建（创建的是不同的对象

        System.out.println(userDao1);//com.meng.dao.impl.UserDaoImpl@6043cd28
        System.out.println(userDao2);//com.meng.dao.impl.UserDaoImpl@cb51256
        System.out.println(userDao1.equals(userDao2));//false

    }

}
/*
多例（懒汉），通过getBean创建的对象不是同一个对象！

userDaoImpl对象被Spring创建
userDaoImpl对象被Spring创建
com.meng.dao.impl.UserDaoImpl@6043cd28
com.meng.dao.impl.UserDaoImpl@cb51256
false
*/
```



