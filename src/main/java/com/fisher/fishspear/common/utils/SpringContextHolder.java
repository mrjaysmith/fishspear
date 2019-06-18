package com.fisher.fishspear.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring的ApplicationContext的持有者,可以用静态方法的方式获取spring容器中的bean
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringContextHolder() {
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    private static void assertApplicationContext() {
        if (applicationContext == null) {
            throw new RuntimeException("applicationContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }
}
