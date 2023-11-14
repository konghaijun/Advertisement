package com.my.ad.controller;


import com.alibaba.fastjson.JSON;
import com.my.ad.annotation.IgnoreResponseAdvice;
import com.my.ad.client.SponsorClient;
import com.my.ad.client.vo.AdPlan;
import com.my.ad.client.vo.AdPlanGetRequest;
import com.my.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@Slf4j
@RestController
public class SearchController {


/*

    // 通过@Autowired注解自动装配SponsorClient bean，用于调用广告赞助服务
    @Autowired
    private SponsorClient sponsorClient;

    // @IgnoreResponseAdvice注解告诉Spring框架不对该方法的返回结果进行全局响应处理
    @IgnoreResponseAdvice
// @RequestMapping注解指定处理"/getAdPands"路径的HTTP请求
    @RequestMapping("/getAdPands")
// getAdPlans方法接收一个HTTP请求体中的JSON数据，并将其转换为AdPlanGetRequest对象，然后返回一个包含AdPlan对象列表的CommonResponse对象
    public CommonResponse<List<AdPlan>> getAdPlans(
            @RequestBody AdPlanGetRequest request
    ){
        // 记录一条日志，然后调用sponsorClient的getAdPlans方法来获取广告计划，并将结果返回给调用方
        log.info("ad-search: getAdPlans ->{}", JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);
    }

*/


}
