# gateway-service

## 启动

```bash
mvn spring-boot:run
```

## NACOS配置
```yaml
# api-gateway.yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true # 启用服务发现
          lower-case-service-id: true # 服务名小写
      routes:
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/auth-service/**
          filters:
            - StripPrefix=1
        - id: service1
          uri: lb://service1  # 使用服务名进行负载均衡
          predicates:
            - Path=/service1/**
          filters:
            - StripPrefix=1
        - id: service2
          uri: lb://service2  # 使用服务名进行负载均衡
          predicates:
            - Path=/service2/**
          filters:
            - StripPrefix=1

# 认证白名单配置
gateway:
  auth:
    exclude-paths:
      - path: "/service1/api/books"
        methods: [GET]
      - path: "/service2/api/books**"
        methods: [GET]
      - path: "/auth-service/api/books"
        methods: [GET]

management:
  endpoints:
    web:
      exposure:
        include: "*" 

auth:
  name: appletest001
```