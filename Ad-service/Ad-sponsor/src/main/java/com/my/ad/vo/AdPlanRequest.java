package com.my.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;


@Data  // Lombok 注解，自动生成 getter、setter、equals、hashCode 等方法
@NoArgsConstructor  // Lombok 注解，生成一个无参构造方法
@AllArgsConstructor  // Lombok 注解，生成一个包含所有参数的构造方法

public class AdPlanRequest {

    private Long id;  // 广告计划的唯一标识符
    private Long userId;  // 用户ID
    private String planName;  // 计划名称
    private String startDate;  // 计划开始日期
    private String endDate;  // 计划结束日期

    public boolean createValidate() {
        // 用于验证创建广告计划时的参数是否有效
        return userId != null
                && !StringUtils.isEmpty(planName)
                && !StringUtils.isEmpty(startDate)
                && !StringUtils.isEmpty(endDate);
    }

    public boolean updateValidate() {
        // 用于验证更新广告计划时的参数是否有效
        return id != null && userId != null;
    }

    public boolean deleteValidate() {
        // 用于验证删除广告计划时的参数是否有效
        return id != null && userId != null;
    }
}
