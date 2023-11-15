package com.my.ad.index.keyword;

import  com.my.ad.index.IndexAware;
import  com.my.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component


public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {



    //  倒排索引 存储关键词到广告单元ID集合的映射关系
    private static Map<String, Set<Long>> keywordUnitMap;
    //  正向索引 存储广告单元ID到关键词集合的映射关系
    private static Map<Long, Set<String>> unitKeywordMap;

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }

        // 根据关键词获取对应的广告单元ID集合
        Set<Long> result = keywordUnitMap.get(key);
        if (result == null) {
            return Collections.emptySet();
        }

        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);

        // 根据关键词获取对应的广告单元ID集合，若不存在则创建新的集合
        Set<Long> unitIdSet = CommonUtils.getorCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );

        unitIdSet.addAll(value);

        //keywordUnitMap 中获取与 key 对应的广告单元ID集合 unitIdSet，如果不存在则创建一个新的集合，并将传入的 value 集合中的所有元素添加到 unitIdSet 中。


        // 更新广告单元ID到关键词集合的映射关系
        for (Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getorCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.add(key);
        }

/*        假设传入的参数为key = "电视"，value = [1001, 1002, 1003]。

        初始状态下，keywordUnitMap和unitKeywordMap为空。

        执行代码逻辑如下：

        获取或创建关键词"电视"对应的广告单元ID集合：

        unitIdSet = CommonUtils.getorCreate("电视", keywordUnitMap, ConcurrentSkipListSet::new)
        创建一个新的ConcurrentSkipListSet集合并放入keywordUnitMap。
        现在keywordUnitMap为{"电视" -> [1001, 1002, 1003]}。
        更新广告单元ID到关键词集合的映射关系：

        对于每个广告单元ID unitId，获取或创建与之对应的关键词集合：
        对于unitId = 1001，获取或创建关键词集合keywordSet = CommonUtils.getorCreate(1001, unitKeywordMap, ConcurrentSkipListSet::new)，
        创建一个新的ConcurrentSkipListSet集合并放入unitKeywordMap。

        现在unitKeywordMap为{1001 -> ["电视"]}。

        类似地，对于unitId = 1002和unitId = 1003也进行相同的操作，最终得到unitKeywordMap为{1001 -> ["电视"], 1002 -> ["电视"], 1003 -> ["电视"]}。
        完成以上操作后，keywordUnitMap和unitKeywordMap的状态如下：
        keywordUnitMap: {"电视" -> [1001, 1002, 1003]}

        unitKeywordMap: {1001 -> ["电视"], 1002 -> ["电视"], 1003 -> ["电视"]}
        这样，就建立了关键词"电视"与广告单元ID集合 [1001, 1002, 1003]之间的映射关系，同时也建立了广告单元ID与关键词集合之间的映射关系。
        这样的映射关系在广告系统中能够支持根据关键词快速查找相关的广告单元，或者根据广告单元ID快速查找相关的关键词。*/


        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        // 关键词索引不支持更新操作
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);

        // 根据关键词获取对应的广告单元ID集合
        Set<Long> unitIds = CommonUtils.getorCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        // 更新广告单元ID到关键词集合的映射关系
        for (Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getorCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.remove(key);
        }

        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);
    }

    // 判断广告单元是否与关键词匹配
    public boolean match(Long unitId, List<String> keywords) {
        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))) {
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            // 判断关键词列表是否是广告单元的子集
            return CollectionUtils.isSubCollection(keywords, unitKeywords);
        }
        return false;
    }
}
