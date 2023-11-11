package com.my.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    // Web配置类，实现了WebMvcConfigurer接口

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 重写configureMessageConverters方法，用于配置消息转换器

        converters.clear(); // 清空默认的消息转换器
        converters.add(new MappingJackson2HttpMessageConverter()); // 添加MappingJackson2HttpMessageConverter作为消息转换器
    }
}
