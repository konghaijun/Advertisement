package com.my.ad.utils;

import com.my.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;


public class CommonUtils {

    // 定义日期格式的数组，用于解析日期字符串
    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"};

    /**
     * 对输入的字符串进行MD5加密，并返回大写形式的加密结果
     *
     * @param value 要进行MD5加密的字符串
     * @return 加密后的结果
     */
    public static String md5(String value) {
        return DigestUtils.md5Hex(value).toUpperCase();
    }

    /**
     * 将日期字符串解析为Date对象
     *
     * @param dateString 待解析的日期字符串
     * @return 解析后得到的Date对象
     * @throws AdException 如果解析过程中发生异常，则抛出AdException
     */
    public static Date parseStringDate(String dateString) throws AdException {
        try {
            // 使用预定义的日期格式数组来尝试解析日期字符串
            return DateUtils.parseDate(dateString, parsePatterns);
        } catch (Exception ex) {
            // 捕获异常并抛出自定义的AdException异常
            throw new AdException(ex.getMessage());
        }
    }

}
