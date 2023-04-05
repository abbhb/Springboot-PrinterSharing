package com.qc.printers.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    //根据bean name 获取实例
    public static Object getBeanByName(String beanName) {
        if (beanName == null || applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }
    //只适合一个class只被定义一次的bean（也就是说，根据class不能匹配出多个该class的实例）
    public static Object getBeanByType(Class clazz) {
        if (clazz == null || applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }
    public static String[] getBeanDefinitionNames() {
        return applicationContext.getBeanDefinitionNames();
    }
}