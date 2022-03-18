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

Bean的实例化个数：1个

Bean的实例化时机：当Spring核心文件被加载时，实例化配置的Bean实例

Bean的生命周期：

- 对象创建：当应用加载，创建容器时，对象就被创建了
- 对象运行：只要容器在，对象就一直运行
- 对象销毁：当应用卸载，销毁容器时，对象就被销毁了

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

Bean的实例化个数：多个

Bean的实例化时机：当调用getBean()方法实例化Bean

Bean的生命周期：

- 对象创建：当使用对象时，创建新的对象实例
- 对象运行：只要对象在使用中，就一直活着
- 对象销毁：当对象长时间不用时，被Java的垃圾回收器回收了

##### Bean生命周期配置

- init-method：指定类中的初始化方法名称
- destroy-method：指定类中的销毁方法名称

```xml
<bean id="userDao" class="com.meng.dao.impl.UserDaoImpl" scope="singleton" init-method="init"
          destroy-method="destroy"></bean>

```

```java
package com.meng.dao.impl;

import com.meng.dao.UserDao;

public class UserDaoImpl implements UserDao {
    public UserDaoImpl() {
        System.out.println("userDaoImpl对象被Spring创建");
    }

    public void init() {
        System.out.println("初始化方法");
    }

    public void destroy() {
        System.out.println("销毁方法");
    }

    @Override
    public void save() {
        System.out.println("saveRunning...");
    }
}

```

##### Bean实例化的三种方式

- 无参**构造**方法实例化（重点掌握）
- 工厂**静态**方法实例化(factory-method)
- 工厂**实例**方法实例化

```xml
静态方法
<bean id="factoryUserDao" class="com.meng.factory.StaticFactory" scope="singleton"
          factory-method="getUserDao"></bean>
```

```xml
实例方法
<bean id="factory" class="com.meng.factory.DynamicFactory" scope="singleton"></bean>
    <bean id="userDao" factory-bean="factory" factory-method="getUserDao"></bean>
```





#### Bean的依赖注入

依赖注入：它是Spring框架核心IOC的具体的实现

在编写程序时，通过控制反转，把对象的创建交给了Spring，但是代码中不可能出现没有依赖的情况。

IOC解耦只是降低他们的依赖关系，但不会消除。例如：业务层仍会调用持久层的方法

那这种业务层和持久层的依赖关系，在使用Spring之后，就让Spring来维护了。

简单的说，就是坐等框架把持久层对象传入到业务层，而不用我们自己去获取



```xml
<bean id="userDao" class="com.meng.dao.impl.UserDaoImpl"></bean>

    <bean id="userService" class="com.meng.service.impl.UserServiceImpl">
        <property name="userDao" ref="userDao"></property>
    </bean>
```

```java
package com.meng.service.impl;

import com.meng.dao.UserDao;
import com.meng.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
    }

    //构造参数注入
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    //set方法注入
    //    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }

    @Override
    public void save() {
        userDao.save();
    }
}

```



##### Bean的依赖注入的数据类型

上面的操作，都是注入的引用Bean，除了对象的引用可以注入，普通数据类型，集合等都可以在容器中进行注入

注入数据的三种数据类型

- 普通数据类型

  ```xml
  <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl">
          <property name="username" value="小胡"></property>
          <property name="age" value="20"></property>
      </bean>
  ```

- 引用数据类型：ref

  ```xml
  <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl">
          <property name="username" value="小胡"></property>
          <property name="age" value="20"></property>
      </bean>
      <bean id="userService" class="com.meng.service.impl.UserServiceImpl">
          <constructor-arg name="userDao" ref="userDao"></constructor-arg>
      </bean>
  ```

- 集合数据类型

  ```xml
  <bean id="userDao" class="com.meng.dao.impl.UserDaoImpl">
          <property name="username" value="小胡"></property>
          <property name="age" value="20"></property>
          <property name="strList">
              <list>
                  <value>aaa</value>
                  <value>bbb</value>
                  <value>ccc</value>
              </list>
          </property>
          <property name="userMap">
              <map>
                  <entry key="user1" value-ref="user"></entry>
              </map>
          </property>
          <property name="properties">
              <props>
                  <prop key="username">root</prop>
                  <prop key="password">12345678</prop>
  
              </props>
          </property>
      </bean>
      <bean id="user" class="com.meng.pojo.User">
          <property name="name" value="xiaohu"></property>
          <property name="addr" value="安徽"></property>
      </bean>
  ```



#### 引入其他配置文件（分模块开发）

实际开发中，Spring的配置内容非常多，这就导致Spring配置很繁琐且**体积很大**，所以，可以将部分配置拆解到其他配置文件中，而在Spring主配置文件通过import标签进行加载

```xml
		<import resource="applicationContext-user.xml"></import>
    <import resource="applicationContext-userDaoImpl.xml"></import>
```





### Spring的重点配置

```xmlm
<bean>标签
	id属性：在容器中Bean实例的唯一标识，不允许重复
	class属性：要实例化的Bean的全限定名
	scope属性：Bean的作用范围，常用的是Singleton（默认单例）和prototype（多例）
	<property>标签：属性注入
		name属性：属性名称
		value属性：注入的普通属性值
		ref属性：注入的对象引用值
		<list>标签：
			<value>aaa</value>
		<map>标签：
			<entry key="user1" value-ref="user"></entry>
		<properties>标签：
			<props>
      	<prop key="username">root</prop>
      </props>
	<constructor-arg>标签
<import>标签：导入其他的Spring的分配置文件
```





### Spring相关API

#### ApplicationContext的继承体系

**applicationContext**：接口类型，代表应用上下文，可以通过其 实例获得Spring容器的Bean对象

1. ClassPathXmlApplicationContext

   它是从类的根路径下加载配置文件推荐使用这个

2. FileSystemXmlApplicationContext

   它是从磁盘路径上加载配置文件，配置文件可以在磁盘的任意位置（不推荐使用）

3. AnnotationConfigApplicationContext

   当使用注解配置容器对象时，需要使用此类来创建Spring容器，用它来读取注解（后期推荐使用）



#### getBean()方法使用

getBean方法有很多重载

常用的如下：

- public Object getBean(String name)

- public < T > T getBean(Class< T > requiredType)

- ```java
  <T> T getBean(String name, Class<T> requiredType)//推荐使用
  ```





## Spring配置数据源

### 数据源（连接池）的作用

- 数据源（连接池）是提高程序性能的
- 首先实例化数据源，初始化部分连接资源
- 使用连接资源时从数据源中获取
- 使用完毕之后将连接资源归还给数据源

常见的数据源（连接池）：DBCP、C3P0、BoneCP、Druid等



### 数据源的开发步骤

1. 导入数据源的坐标和数据库驱动坐标
2. 创建数据源对象
3. 设置数据源的基本的连接数据
4. 使用数据源获取连接资源和归还连接资源

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.meng</groupId>
    <artifactId>spring_ioc_anno</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
        <dependency>
            <groupId>c3p0</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.1.2</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.2.8</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql:///db1
jdbc.username=root
jdbc.password=12345678
```

```java
package com.meng.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataSourceTest {

    /**
     * 测试手动创建C3P0数据源
     */
    @Test
    public void test1() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:///db1");
        dataSource.setUser("root");
        dataSource.setPassword("12345678");
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

        connection.close();

    }

    /**
     * 测试手动创建C3P0数据源(加载配置文件）
     */
    @Test
    public void test3() throws Exception {
        //1、读取配置文件
        ResourceBundle rb = ResourceBundle.getBundle("jdbc");
        String driver = rb.getString("jdbc.driver");
        String url = rb.getString("jdbc.url");
        String username = rb.getString("jdbc.username");
        String password = rb.getString("jdbc.password");
        //2、创建数据源对象
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);

        connection.close();


    }

    /**
     * Druid数据源
     */
    @Test
    public void test2() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///db1");
        dataSource.setUsername("root");
        dataSource.setPassword("12345678");
        DruidPooledConnection connection = dataSource.getConnection();

        System.out.println(connection);

        connection.close();
    }
}

```





### Spring配置数据源

我们发现创建数据源是调用该数据源的无参构造，并且通过set方法进行设置参数，这符合Spring的配置文件，所以我们可以通过Spring的配置文件进行配置数据源

可以将DataSource的创建权交给Spring容器去完成

```xml
		<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.cj.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql:///db1"></property>
        <property name="user" value="root"></property>
        <property name="password" value="12345678"></property>
    </bean>
```

```java
@Test
    public void test4() throws SQLException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ComboPooledDataSource dataSource = applicationContext.getBean("dataSource", ComboPooledDataSource.class);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }
```

