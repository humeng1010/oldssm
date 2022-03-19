package com.meng.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //将Spring的应用上下文对象存储到ServletContext域中
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("app",applicationContext);

        System.out.println("spring容器创建完毕");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
