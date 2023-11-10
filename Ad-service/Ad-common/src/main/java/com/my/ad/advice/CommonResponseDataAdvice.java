package com.my.ad.advice;

import com.my.ad.annotation.IgnoreResponseAdvice;
import com.my.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    // 判断是否支持对响应体的处理
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果控制器类或方法上标记了IgnoreResponseAdvice注解，则不处理响应
        if (methodParameter.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )) {
            return false;
        }

        if (methodParameter.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )) {
            return false;
        }

        return true; // 其他情况均进行处理
    }

    // 在响应体写入之前进行处理
    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> response = new CommonResponse<>(0, ""); // 创建通用响应对象
        if (null == o) {
            return response; // 如果响应体为空，则返回默认通用响应
        } else if (o instanceof CommonResponse) {
            response = (CommonResponse<Object>) o; // 如果响应体已经是CommonResponse类型，则直接使用
        } else {
            response.setData(o); // 否则将响应体作为数据放入通用响应对象
        }
        return response; // 返回经过处理后的通用响应对象
    }
}
