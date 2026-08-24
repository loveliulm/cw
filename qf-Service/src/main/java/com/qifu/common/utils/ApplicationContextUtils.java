package com.qifu.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtils.applicationContext == null)
            ApplicationContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取bean对象
    public static Object getBean(String name) {
        if (getApplicationContext() != null) {
            try {
                return getApplicationContext().getBean(name);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
        return null;
    }
    //通过class获取bean对象
    public static <T> T getBean(Class<T> beanClass) {
        if (getApplicationContext() != null) {
            return getApplicationContext().getBean(beanClass);
        }
        return null;
    }
}
