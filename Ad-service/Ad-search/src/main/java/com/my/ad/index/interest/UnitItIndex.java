package com.my.ad.index.interest;

import  com.my.ad.index.IndexAware;
import  com.my.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {
    // 反向索引，用于存储兴趣标签和广告单元ID集合的映射关系
    private static Map<String, Set<Long>> itUnitMap;

    // 正向索引，用于存储广告单元ID和兴趣标签集合的映射关系
    private static Map<Long, Set<String>> unitItMap;

    static {
        // 初始化反向索引和正向索引
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
// 根据兴趣标签获取对应的广告单元ID集合
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
// 添加兴趣标签与广告单元ID集合的映射关系
    public void add(String key, Set<Long> value) {
        log.info("UnitItIndex, before add: {}", unitItMap);

        // 获取或创建与兴趣标签对应的广告单元ID集合，并将新的广告单元ID集合添加进去
        Set<Long> unitIds = CommonUtils.getorCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);

        // 遍历广告单元ID集合，为每个广告单元ID更新与之对应的兴趣标签集合
        for (Long unitId : value) {
            Set<String> its = CommonUtils.getorCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            its.add(key);
        }

        log.info("UnitItIndex, after add: {}", unitItMap);
    }

    @Override
// 更新兴趣标签与广告单元ID集合的映射关系（此处不支持更新）
    public void update(String key, Set<Long> value) {
        log.error("it index can not support update");
    }

    @Override
// 删除兴趣标签与广告单元ID集合的映射关系
    public void delete(String key, Set<Long> value) {
        log.info("UnitItIndex, before delete: {}", unitItMap);

        // 获取与兴趣标签对应的广告单元ID集合，并删除指定的广告单元ID
        Set<Long> unitIds = CommonUtils.getorCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(value);

        // 遍历要删除的广告单元ID集合，更新与之对应的兴趣标签集合
        for (Long unitId : value) {
            Set<String> itTagSet = CommonUtils.getorCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            itTagSet.remove(key);
        }

        log.info("UnitItIndex, after delete: {}", unitItMap);
    }

    // 匹配广告单元ID与兴趣标签集合是否符合
    public boolean match(Long unitId, List<String> itTags) {
        // 判断广告单元ID是否存在于正向索引中，并且对应的兴趣标签集合非空
        if (unitItMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitItMap.get(unitId))) {
            Set<String> unitKeywords = unitItMap.get(unitId);
            // 判断传入的兴趣标签集合是否是对应广告单元ID的子集
            return CollectionUtils.isSubCollection(itTags, unitKeywords);
        }
        return false;
    }

}
