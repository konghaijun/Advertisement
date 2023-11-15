package com.my.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;


@Slf4j
public class CommonUtils {
    public static <K, V> V getorCreate(K key, Map<K, V> map,
                                       Supplier<V> factory) {
        // 使用 computeIfAbsent 方法，如果指定的 key 在 map 中不存在，则使用 factory 创建一个新的 value，并将其与 key 关联起来
        // 如果指定的 key 在 map 中已经存在，则返回与 key 关联的 value
        return map.computeIfAbsent(key, k -> factory.get());
    }

    public static String stringConcat(String... args) {

        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            // 将每个参数添加到 result 中
            result.append(arg);
            // 添加连接符 "-"
            result.append("-");
        }
        // 删除最后一个连接符 "-"
        result.deleteCharAt(result.length() - 1);
        // 将 StringBuilder 对象转换为 String 类型并返回
        return result.toString();
    }

    // 将字符串日期解析为 Date 对象
    public static Date parseStringDate(String dateString) {

        try {

            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US
            );
            // 解析 dateString，并根据时区进行调整
            return DateUtils.addHours(
                    dateFormat.parse(dateString),
                    -8
            );

        } catch (ParseException ex) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }

}
