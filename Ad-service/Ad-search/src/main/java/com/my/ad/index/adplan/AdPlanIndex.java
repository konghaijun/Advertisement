package com.my.ad.index.adplan;

import  com.my.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j // 使用Lombok库自动生成日志记录器
@Component // 声明该类为Spring组件，可以被自动扫描和注入
public class AdPlanIndex implements IndexAware<Long, AdPlanObject> {

    private static Map<Long, AdPlanObject> objectMap; // 使用ConcurrentHashMap作为内部存储的对象Map

    // 在静态代码块中初始化objectMap
    static {
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key); // 根据key获取对应的AdPlanObject对象
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("before add: {}", objectMap); // 打印添加前的objectMap日志
        objectMap.put(key, value); // 将key-value对添加到objectMap中
        log.info("after add: {}", objectMap); // 打印添加后的objectMap日志
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("before update: {}", objectMap); // 打印更新前的objectMap日志

        AdPlanObject oldObject = objectMap.get(key); // 根据key获取旧的AdPlanObject对象
        if (null == oldObject) {
            objectMap.put(key, value); // 如果旧的对象不存在，直接将新的value添加到objectMap中
        } else {
            oldObject.update(value); // 如果旧的对象存在，调用其update方法更新属性
        }

        log.info("after update: {}", objectMap); // 打印更新后的objectMap日志
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("before delete: {}", objectMap); // 打印删除前的objectMap日志
        objectMap.remove(key); // 根据key从objectMap中删除对应的元素
        log.info("after delete: {}", objectMap); // 打印删除后的objectMap日志
    }
}
