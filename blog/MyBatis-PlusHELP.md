### MyBatis-Plus学习笔记

我们在开发中使用MyBatis-Plus可以大大简化我们的开发，提高开发效率

- 注意：导入了plus就不需要导入mybatis了
- 我们使用3.0.5版本的原生的版本，可以更好的研究本质源码
- 根据官方的教程来进行学习：https://baomidou.com/pages/226c21/
- 导入依赖
```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>
```
进行学习吧！

#### 传统方式pojo-dao-service-controller（mybatis）

#### plus！！：pojo-mapper接口-使用（plus的高效率）
##快速入门
```java
package com.meng.mybatisplus.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String email;

}

```

```java
package com.meng.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meng.mybatisplus.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * 使用了MyBatis之后，只需要在dao层的接口后面让它 继承 BaseMapper!!! 超级简单的，不需要映射sql的xml文件！！
 * 注意需要添加一个约束的泛型,告诉他要去操作什么类型
 * 已经ok了
 */

@Repository//表示这个是dao层的注解，就是把这个接口注册到spring容器中交给容器管理
public interface UserMapper extends BaseMapper<User> {
}

```
```java
package com.meng.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//扫描mapper文件夹
@MapperScan("com.meng.mybatisplus.mapper")
@SpringBootApplication
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}

```
```java
package com.meng.mybatisplus;

import com.meng.mybatisplus.mapper.UserMapper;
import com.meng.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

}

```
- 注意点：我们需要在主启动类上去扫描我们的mapper包下的所有接口
```java
//扫描mapper文件夹
@MapperScan("com.meng.mybatisplus.mapper")
@SpringBootApplication
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
```
查询完毕：

User(id=1, name=Jone, age=18, email=test1@baomidou.com)
User(id=2, name=Jack, age=20, email=test2@baomidou.com)
User(id=3, name=Tom, age=28, email=test3@baomidou.com)
User(id=4, name=Sandy, age=21, email=test4@baomidou.com)
User(id=5, name=Billie, age=24, email=test5@baomidou.com)

问题？
1. SQL谁帮我们写的？当然是MP（mybatis-plus）
2. 方法是哪里来的？还是MP都写好的，我们直接使用即可


##配置日志
由于我们的SQL现在是不可见的，我们希望知道他是专门执行的，所以我们需要使用日志，开发的时候可以开启日志，项目上线的时候，日志需要关闭
```yaml
#配置MP的日志
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl #默认的控制台输出，其他的log4j，slf4j需要导入对应的依赖
    

```
配置完毕日志后，后面的学习就需要注意这个自动生成的SQL

##CRUD扩展
###插入操作
> insert功能
```java
package com.meng.mybatisplus;

import com.meng.mybatisplus.mapper.UserMapper;
import com.meng.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;
    
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

}

```
> 插入的ID默认值为：全局的唯一ID

###主键生成的策略
雪花算法：
通过一系列的数据结构的算法得到一个ID，可以保证几乎全球唯一！！
```java
package com.meng.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //对应数据库中的主键（uuid,自增id，【雪花算法】，redis，zookeeper）
    //MP中的注解
//    @TableId(type = IdType.ID_WORKER)//默认的全局唯一
    @TableId(type = IdType.AUTO)//自增的，注意：要求数据库中的主键一定要是自增的！！
    private Long id;
    private String name;
    private Integer age;
    private String email;

}

```
源码：
```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.baomidou.mybatisplus.annotation;

public enum IdType {
    AUTO(0),//数据库ID自增
    NONE(1),//未设置主键
    INPUT(2),//手动输入（就需要自己配置id）
    ID_WORKER(3),//默认的全局唯一ID
    UUID(4),//全局唯一ID uuid
    ID_WORKER_STR(5);//ID_WORKER字符串表示

    private int key;

    private IdType(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

```

###更新
>更新

```java
package com.meng.mybatisplus;

import com.meng.mybatisplus.mapper.UserMapper;
import com.meng.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;
    

    /**
     * 测试更新
     */
    @Test
    public void testUpdate(){
        User user = new User();//会帮我们自动生成id！
        //通过条件拼接 动态SQL ！！!
        user.setId(1L);//一号数据
        user.setName("xiaohu");
        user.setAge(20);
        user.setEmail("1589029587@qq.com");
        //注意：虽然说名字是UpdateById，但是需要传入的参数是一个User对象
        int count = userMapper.updateById(user);//受影响的行数
        System.out.println(count);

    }

}

```

###自动填充
创建时间，修改时间！ 这些操作一般都是自动化完成的，我们不希望手动更新

阿里巴巴开发手册：所有的数据库表：gmt_create,gmt_modified，几乎所有的表都要必备配置，而且需要自动化！！！

方式一：数据库级别（工作中不允许使用，因为修改了数据库）
1. 在表中新增字段 create_time , update_time
默认值为：CURRENT-TIMESTAMP
2. 把实体类同步
>    private Date createTime;//创建时间，和数据库中的名称不一样
private Date updateTime;//更新时间，和数据库中的名称不一样（下划线-》驼峰）
3. 测试方法

方式二：代码级别
1. 删除数据库的默认值、更新操作
2. 实体类字段属性上需要增加注解
```java
package com.meng.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //对应数据库中的主键（uuid,自增id，【雪花算法】，redis，zookeeper）
    //MP中的注解
//    @TableId(type = IdType.ID_WORKER)//默认的全局唯一
    @TableId(type = IdType.AUTO)//自增的，注意：要求数据库中的主键一定要是自增的！！
    private Long id;
    private String name;
    private Integer age;
    private String email;

    //字段添加填充内容
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)//更新要用这个，否则插入的时间会变成null
    private Date updateTime;//更新时间

}


```
3. 编写处理器来处理这个注解即可
```java
package com.meng.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j//日志
@Component//丢到spring容器中
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时的填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill...");
        //setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
        this.setFieldValByName("createTime",new Date(),metaObject);
    }

    /**
     * 更新时的填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill...");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}

```

###MP官方的自动填充文档
自动填充功能

示例工程：

👉 mybatis-plus-sample-auto-fill-metainfo (opens new window)

原理:

实现元对象处理器接口：com.baomidou.mybatisplus.core.handlers.MetaObjectHandler

注解填充字段 @TableField(.. fill = FieldFill.INSERT) 生成器策略部分也可以配置！

```java
public class User {

    // 注意！这里需要标记为填充字段
    @TableField(fill = FieldFill.INSERT)
    private String fillField;

    
}
```


自定义实现类 MyMetaObjectHandler
```java
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        // 或者
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "createTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
        // 或者
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }
}
```


注意事项：

填充原理是直接给entity的属性设置值!!!

注解则是指定该属性在对应情况下必有值,如果无值则入库会是null

MetaObjectHandler提供的默认方法的策略均为:如果属性有值则不覆盖,如果填充值为null则不填充

字段必须声明TableField注解,属性fill选择对应策略,该声明告知Mybatis-Plus需要预留注入SQL字段

填充处理器MyMetaObjectHandler在 Spring Boot 中需要声明@Component或@Bean注入

要想根据注解FieldFill.xxx和字段名以及字段类型来区分必须使用父类的strictInsertFill或者strictUpdateFill方法

不需要根据任何来区分可以使用父类的fillStrategy方法

update(T t,Wrapper updateWrapper)时t不能为空,否则自动填充失效


```java
public enum FieldFill {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入填充字段
     */
    INSERT,
    /**
     * 更新填充字段
     */
    UPDATE,
    /**
     * 插入和更新填充字段
     */
    INSERT_UPDATE
}


```

###乐观锁
在面试中经常会被问到
>乐观锁：顾名思义，他认为不会出现问题，无论干什么都不去上锁，如果出现问题，再次更新值测试

>悲观锁：顾名思义，他认为干什么事情总会出现问题，所有无论干什么都会上锁，再去操作

乐观锁：
version 、 new version
乐观锁的实现方式
- 取出记录时，获取当前 version
- 更新时，带上这个 version
- 执行更新时， set version = newVersion where version = oldVersion
- 如果 version 不对，就更新失败

```sql
-- 乐观锁：1、先查询，获得版本号 version = 1
-- A线程
update user set name = 'hu', version = version + 1 where id = 2 and version = 1
-- B线程 抢先完成，这个时候版本号就已经修改了，导致version = 2，会导致A线程修改失败
update user set name = 'hu', version = version + 1 where id = 2 and version = 1
```

####测试MP的乐观锁插件
1. 给数据库中增加version字段(默认值为1)
2. 我们需要实体类加对应的字段
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //对应数据库中的主键（uuid,自增id，【雪花算法】，redis，zookeeper）
    //MP中的注解
//    @TableId(type = IdType.ID_WORKER)//默认的全局唯一
    @TableId(type = IdType.AUTO)//自增的，注意：要求数据库中的主键一定要是自增的！！
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @Version //乐观锁的version注解(防止数据多线程修改引发的错误）
    private Integer version;

    //字段添加填充内容
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间

}

```
3. 注册组件
```java
//扫描mapper文件夹
@MapperScan("com.meng.mybatisplus.mapper")

@EnableTransactionManagement//开启自动管理事务
@Configuration//配置类
public class MyBatisPlusConfig {
    /**
     * 旧版
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}
```
4. 测试
```java
@SpringBootTest
class MybatisPlusApplicationTests {
    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;
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
}
```

### 查询操作
测试查询：
```java
@SpringBootTest
class MybatisPlusApplicationTests {

    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;
    /**
     * 查询操作
     */
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1L);
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


}

```

###分页查询
MP其实内置了分页插件，十分方便
1. 配置拦截器组件即可
```java
//扫描mapper文件夹
@MapperScan("com.meng.mybatisplus.mapper")

@EnableTransactionManagement//开启自动管理事务
@Configuration//配置类
public class MyBatisPlusConfig {
    /**
     * 乐观锁
     * 旧版
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}

```
2. 直接使用Page对象即可
```java
@SpringBootTest
class MybatisPlusApplicationTests {

    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;
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
}
```
###删除
基本的删除操作
```java
@SpringBootTest
class MybatisPlusApplicationTests {

    /**
     * 继承了BaseMapper，所有的方法都来自父类
     * 我们也可以编写自己的扩展方法
     */
    @Autowired
    private UserMapper userMapper;
    /**
     * 删除
     */
    //根据ID删除
    @Test
    public void testDeleteById(){
        int count = userMapper.deleteById(1506866823759511556L);//返回受影响的行数
        System.out.println(count);
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

```

但是基本的删除在工作中会遇到问题：逻辑删除

###逻辑删除
物理删除：从数据库中直接被移除

逻辑删除：再数据库中没有被移除，而是通过变量来让它失效！delete=0 => delete=1

管理员可以查看被删除的记录！防止数据的丢失，就类似于回收站！

测试：
1. 在数据表中增加一个delete字段
```sql
alter table user
    add `deleted` int(1) default 0 null comment '逻辑删除';
```
默认值为0（存在，可以被查询到）
执行删除其实本质就是修改deleted为1（不存在，不可以被查询到）
2. pojo实体类中增加属性，并配套注解
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //对应数据库中的主键（uuid,自增id，【雪花算法】，redis，zookeeper）
    //MP中的注解
//    @TableId(type = IdType.ID_WORKER)//默认的全局唯一
    @TableId(type = IdType.AUTO)//自增的，注意：要求数据库中的主键一定要是自增的！！
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @Version //乐观锁的version注解(防止数据多线程修改引发的错误）
    private Integer version;

    @TableLogic//逻辑删除
    private Integer deleted;

    //字段添加填充内容
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间
}

```
3. 配置！
```java
//扫描mapper文件夹
@MapperScan("com.meng.mybatisplus.mapper")

@EnableTransactionManagement//开启自动管理事务
@Configuration//配置类
public class MyBatisPlusConfig {
    /**
     * 乐观锁
     * 旧版
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 逻辑删除组件
     */
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }

}
```
```yaml
#配置逻辑删除
  global-config:
    db-config:
      logic-delete-value: 1 #删的值，把这个删除的值修改为0就可以恢复数据，方便管理员操作
      logic-not-delete-value: 0 #没有删除的值
```

4. 测试
```
    /**
     * 删除
     */
    //根据ID删除
    @Test
    public void testDeleteById(){
        userMapper.deleteById(1506866823759511555L);//返回受影响的行数

    }
```
结果：

   ==> Preparing: UPDATE user SET deleted=1 WHERE id=? AND deleted=0

   ==> Parameters: 1506866823759511555(Long)

   <==    Updates: 1

我们执行的是删除操作，但是本质是更新操作

当我们再次查询这个被逻辑删除的ID的时候，我们发现：

==>  Preparing: SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE id=? AND deleted=0

==> Parameters: 1506866823759511555(Long)

<==      Total: 0

它在查询条件的后面自动拼接了一个deleted = 0

注意点：数据库中的deleted要使用deleted的列名，否则会报错，因为delete是SQL的语法关键字

###性能分析插件
我们在平时的开发中，会遇到一些慢sql，慢查询

MP也提供了性能分析插件，如果超过这个时间就停止运行

1. 导入插件
```java
//扫描mapper文件夹
@MapperScan("com.meng.mybatisplus.mapper")

@EnableTransactionManagement//开启自动管理事务
@Configuration//配置类
public class MyBatisPlusConfig {
    /**
     * 乐观锁
     * 旧版
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 逻辑删除组件
     */
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }

    /**
     * SQL执行效率插件
     * @return
     */
    @Bean
    @Profile({"dev","test"})//设置 dev test 环境开启，保证我们的效率
    public PerformanceInterceptor performanceInterceptor(){
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(100);//设置SQL执行的最大时间，如果超过了则不执行
        performanceInterceptor.setFormat(true);//开启SQL的格式化，在控制台输出的SQL（通过yaml配置的log）让它格式化
        return new PerformanceInterceptor();
    }


}

```
还要在springboot中配置为dev环境或者测试环境
```yaml
spring:
  #设置开发环境
  profiles:
    active: dev #开发环境
```

2. 测试插入（只要超出了规定的时间会出现异常）

结果：

   ==>  Preparing: INSERT INTO user ( name, age, email, create_time, update_time ) VALUES ( ?, ?, ?, ?, ? )

   ==> Parameters: xiaohu(String), 20(Integer), 1589029587@qq.com(String), 2022-03-24 16:49:54.98(Timestamp), null

   <==    Updates: 1

   Time：12 ms - ID：com.meng.mybatisplus.mapper.UserMapper.insert

   Execute SQL：INSERT INTO user ( name, age, email, create_time, update_time ) VALUES ( 'xiaohu', 20, '1589029587@qq.com', '2022-03-24 16:49:54.98', null )

###条件构造器Wrapper
十分重要：Wrapper

我们写一些复杂的SQL就可以使用它来替代

测试1：
```java
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
}

```
输出结果的SQL
```
==>  Preparing: SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 AND name IS NOT NULL AND email IS NOT NULL AND age >= ? 
==> Parameters: 21(Integer)
<==    Columns: id, name, age, email, version, deleted, create_time, update_time
<==        Row: 3, Tom, 28, test3@baomidou.com, 1, 0, null, null
<==        Row: 4, Sandy, 21, test4@baomidou.com, 1, 0, null, null
<==      Total: 2
 Time：23 ms - ID：com.meng.mybatisplus.mapper.UserMapper.selectList
Execute SQL：SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 AND name IS NOT NULL AND email IS NOT NULL AND age >= 21

```
测试2：
```java
@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;
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
}

```

测试三：
```
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
```
```
==>  Preparing: SELECT COUNT(1) FROM user WHERE deleted=0 AND age BETWEEN ? AND ? 
==> Parameters: 20(Integer), 30(Integer)
<==    Columns: COUNT(1)
<==        Row: 5
<==      Total: 1
 Time：13 ms - ID：com.meng.mybatisplus.mapper.UserMapper.selectCount
Execute SQL：SELECT COUNT(1) FROM user WHERE deleted=0 AND age BETWEEN 20 AND 30
```
测试四：模糊查询！！注意分析输出的SQL
```
    /**
     * 模糊查询
     */
    @Test
    public void test4(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        userQueryWrapper
                .like("name","T")
                .likeRight("email","test");//右：test%

        List<Map<String, Object>> maps = userMapper.selectMaps(userQueryWrapper);
//        System.out.println(maps);
        maps.forEach(System.out::println);
    }
```
结果：

```
==>  Preparing: SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 AND name LIKE ? AND email LIKE ? 
==> Parameters: %T%(String), test%(String)
<==    Columns: id, name, age, email, version, deleted, create_time, update_time
<==        Row: 3, Tom, 28, test3@baomidou.com, 1, 0, null, null
<==      Total: 1
 Time：14 ms - ID：com.meng.mybatisplus.mapper.UserMapper.selectMaps
Execute SQL：SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 AND name LIKE '%T%' AND email LIKE 'test%'
```

测试五：

```
    /**
     * 通过ID进行排序
     * 降序: orderDesc
     * 升序: orderAsc
     */
    @Test
    public void test5(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.orderByDesc("id");
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }
```

结果：

```
==>  Preparing: SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 ORDER BY id DESC 
==> Parameters: 
<==    Columns: id, name, age, email, version, deleted, create_time, update_time
<==        Row: 1506866823759511560, xiaohu, 20, 1589029587@qq.com, 1, 0, 2022-03-24 16:49:55, null
<==        Row: 1506866823759511559, xiaohu, 20, 1589029587@qq.com, 1, 0, 2022-03-24 16:49:00, null
<==        Row: 4, Sandy, 21, test4@baomidou.com, 1, 0, null, null
<==        Row: 3, Tom, 28, test3@baomidou.com, 1, 0, null, null
<==        Row: 2, Jack, 20, test2@baomidou.com, 1, 0, null, null
<==        Row: 1, jackkkk, 19, 12345@qq.com, 8, 0, null, 2022-03-24 15:23:36
<==      Total: 6
 Time：29 ms - ID：com.meng.mybatisplus.mapper.UserMapper.selectList
Execute SQL：SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 ORDER BY id DESC

```


##代码自动生成器（重点）超级方便

AutoGenerator 是 MyBatis-Plus 的代码生成器，通过 AutoGenerator 可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码，极大的提升了开发效率。

