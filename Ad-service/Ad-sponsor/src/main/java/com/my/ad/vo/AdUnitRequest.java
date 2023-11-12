package com.my.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {

    private Long planId; // 广告计划ID
    private String unitName; // 推广单元名称

    private Integer positionType; // 广告投放位置类型
    private Long budget; // 广告预算

    /**
     * 验证要创建的广告单元是否有效
     * @return 如果广告单元有效则返回true，否则返回false
     */
    public boolean createValidate() {
        return null != planId && !StringUtils.isEmpty(unitName)
                && positionType != null && budget != null;
    }

}
