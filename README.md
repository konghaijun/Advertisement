# Advertisement

基于SpringCloud微服务架构实现的广告投放系统

## 广告系统骨架开发

### Eureka的介绍

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
```

```yml
spring:
  application:
    name: Ad-eureka
server:
  port: 8000

eureka:
  instance:
    hostname: localhost
  client:
    fetch-registry: false
    register-with-eureka: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

#---
#spring:
#  application:
#    name: Ad-eureka
#  profiles: server1
#server:
#  port: 8000
#eureka:
#  instance:
#    hostname: server1
#    prefer-ip-address: false
#  client:
#    service-url:
#      defaultZone: http://server2:8001/eureka/,http://server3:8002/eureka/
#
#---
#spring:
#  application:
#    name: Ad-eureka
#  profiles: server2
#server:
#  port: 8001
#eureka:
#  instance:
#    hostname: server2
#    prefer-ip-address: false
#  client:
#    service-url:
#      defaultZone: http://server1:8000/eureka/,http://server3:8002/eureka/
#
#---
#spring:
#  application:
#    name: Ad-eureka
#  profiles: server3
#server:
#  port: 8002
#eureka:
#  instance:
#    hostname: server3
#    prefer-ip-address: false
#  client:
#    service-url:
#      defaultZone: http://server1:8000/eureka/,http://server2:8001/eureka/

```

### Gateway的介绍

```java
@EnableZuulProxy
@SpringCloudApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class,args);
    }
}
```

```yml
server:
  port: 9000
spring:
  application:
    name: Ad-gateway
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8000/eureka/
```

```java
package com.my.ad.filter;

@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("startTime", System.currentTimeMillis());

        return null;
    }
}

```

```java
package com.my.ad.filter;


@Slf4j
@Component
public class AccessLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        Long startTime = (Long) context.get("startTime");
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;

        log.info("uri: " + uri + ", duration: " + duration / 100 + "ms");

        return null;
    }
}

```



## 微服务通用模块开发

