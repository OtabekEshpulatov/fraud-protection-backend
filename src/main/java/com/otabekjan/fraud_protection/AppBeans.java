package com.otabekjan.fraud_protection;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

public class AppBeans {
    @Getter
    private static ApplicationContext applicationContext;

    private static void setApplicationContext(ApplicationContext applicationContext) {
        AppBeans.applicationContext = applicationContext;
    }

    public static <T> T get(String name, Class<T> beanClass) {
        try {
            return applicationContext.getBean(name, beanClass);
        } catch (BeansException e) {
            throw new RuntimeException("Bean not found with name: " + name, e);
        }
    }

    public static <T> T get(Class<T> beanClass) {
        try {
            return applicationContext.getBean(beanClass);
        } catch (BeansException e) {
            throw new RuntimeException("Bean not found", e);
        }
    }

    public static <T> T opt(Class<T> beanClass) {
        try {
            return get(beanClass);
        } catch (Exception e) {
            return null;
        }
    }

    @Component("ApplicationContextRetriever")
    public static class ApplicationContextRetriever implements ApplicationContextAware {
        @Override
        public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
            AppBeans.setApplicationContext(applicationContext);
        }
    }
}