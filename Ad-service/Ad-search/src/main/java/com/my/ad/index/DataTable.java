package com.my.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    // 使用线程安全的 ConcurrentHashMap 存储数据表实例
    public static final Map<Class, Object> dataTableMap =
            new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        // 设置应用程序上下文，以便在需要时获取 Bean 实例
        DataTable.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        // 设置此组件的优先级顺序为最高
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz) {
        // 从 dataTableMap 中获取指定类的实例
        T instance = (T) dataTableMap.get(clazz);
        if (null != instance) {
            // 如果实例已存在，直接返回
            return instance;
        }

        // 如果实例不存在，则通过调用 bean(clazz) 方法创建并添加到 dataTableMap 中
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    @SuppressWarnings("all")
    private static <T> T bean(String beanName) {
        // 通过应用程序上下文获取指定名称的 Bean 实例
        return (T) applicationContext.getBean(beanName);
    }

    @SuppressWarnings("all")
    private static <T> T bean(Class clazz) {
        // 通过应用程序上下文获取指定类的 Bean 实例
        return (T) applicationContext.getBean(clazz);
    }
}
