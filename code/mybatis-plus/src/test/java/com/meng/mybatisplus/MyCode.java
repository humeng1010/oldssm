package com.meng.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * 代码自动生成
 */
public class MyCode {
    public static void main(String[] args) {
        //1、首先构建一个代码自动生成器 对象
        AutoGenerator autoGenerator = new AutoGenerator();
        //配置策略
        //1、全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");//获取当前代码的目录
        globalConfig.setOutputDir(projectPath+"/src/main/java");//输出路径
        globalConfig.setAuthor("xiaohu");//设置作者
        globalConfig.setOpen(false);//是否该打开文件夹
        globalConfig.setFileOverride(false);//是否覆盖原文件
        globalConfig.setServiceName("%sService");//去service的I前缀
        globalConfig.setIdType(IdType.ID_WORKER);//设置主键默认的初始的算法：雪花算法
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setSwagger2(true);//swagger

        autoGenerator.setGlobalConfig(globalConfig);
        //2、设置数据源
        /**
         * datasource:
         *     username: root
         *     password: 12345678
         *     driver-class-name: com.mysql.cj.jdbc.Driver
         *     url: jdbc:mysql:///mybatis
         */
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql:///mybatis");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("12345678");
        dataSourceConfig.setDbType(DbType.MYSQL);//MySQL
        autoGenerator.setDataSource(dataSourceConfig);

        //3、包的配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName("bolg");//设置模块的名字
        packageConfig.setParent("com.meng.mybatisplus");
        packageConfig.setEntity("pojo");
        packageConfig.setMapper("mapper");
        packageConfig.setService("service");
        packageConfig.setController("controller");

        autoGenerator.setPackageInfo(packageConfig);

        //4、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("user");//设置要映射的表名
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);//设置驼峰下划线
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategyConfig.setSuperEntityClass("你自己的实体类，没有就不用设置！！！");
        strategyConfig.setEntityLombokModel(true);//使用Lombok插件
//        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setLogicDeleteFieldName("deleted");
        //自动填充配置
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategyConfig.setTableFillList(tableFills);

        //乐观锁
        strategyConfig.setVersionFieldName("version");

        strategyConfig.setRestControllerStyle(true);//开启RestFull的驼峰命名
        strategyConfig.setControllerMappingHyphenStyle(true);//localhost:8080/hello_id_2


        autoGenerator.setStrategy(strategyConfig);


        autoGenerator.execute();//执行代码构造器


    }
}
