package com.meng.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

//标志——该类是Spring的一个核心配置类
@Configuration
//<!--    配置组件扫描-->
//    <context:component-scan base-package="com.meng"></context:component-scan>
@ComponentScan("com.meng")
@Import({DataSourceConfiguration.class})
public class SpringConfiguration {


}
