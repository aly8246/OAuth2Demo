package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Auther: 南有乔木
 * @Date: 2020/4/13 14:58
 * @Description:
 */
@Configuration
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext;


    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException(
                    "'applicationContext' property is null,ApplicationContextHolder not yet init.");
        }
        return applicationContext;
    }

    /**
     * * 根据bean的id来查找对象
     * * @param id
     * * @return
     */
    public static Object getBeanById(String id) {
        checkApplicationContext();
        return applicationContext.getBean(id);
    }

    /**
     * * 通过名称获取bean
     * * @param name
     * * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        Object object = applicationContext.getBean(name);
        return (T) object;
    }


    /**
     * * 根据bean的class来查找对象
     * * @param c
     * * @return
     */
    @SuppressWarnings("all")
    public static <T> T getBeanByClass(Class<T> c) {
        checkApplicationContext();
        return (T) applicationContext.getBean(c);
    }

    /**
     * * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * 如果有多个Bean符合Class, 取出第一个.
     * * @param cluss
     * * @return
     */
    public static <T> T getBean(Class<T> cluss) {
        checkApplicationContext();
        return (T) applicationContext.getBean(cluss);
    }

    /**
     * * 名称和所需的类型获取bean
     * * @param name
     * * @param cluss
     * * @return
     */
    public static <T> T getBean(String name, Class<T> cluss) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name, cluss);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        checkApplicationContext();
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义ApplicationContextHolderGm");
        }
    }

    @Override
    public void destroy() throws Exception {
        checkApplicationContext();
    }

    /**
     * 清除applicationContext静态变量
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
}
