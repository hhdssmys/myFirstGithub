&emsp;&emsp;Spring Boot Admin 是一个管理和监控Spring Boot 应用程序的开源软件。每个应用都认为是一个客户端，通过HTTP或者使用
Eureka注册到admin server中进行展示，Spring Boot Admin UI部分使用VueJs将数据展示在前端。  
&emsp;&emsp;Spring Boot Admin 是一个针对spring-boot的actuator接口进行UI美化封装的监控工具。他可以：在列表中浏览所有被监控
spring-boot项目的基本信息，详细的Health信息、内存信息、JVM信息、垃圾回收信息、各种配置信息（比如数据源、  
缓存列表和命中率）等，还可以直接修改logger的level。
&emsp;&emsp;其包括 server 和 client 两部分。但是目前没有过多研究，只能实现简单的配置，如下：  

+ #### server 配置（没有加上 security,boot.admin.context-path 自定义的管控台上下文,必须得加上 spring-boot-admin-server-ui 依赖）
```
server:
  port: 8000
spring: (无关痛痒)
  application:
    name: Spring-Boot-Admin-Web
```
- #### client 配置（）
```
server:
  port: 8080
  servlet:
    context-path: /demo
spring:
  application:
    # Spring Boot Admin展示的客户端项目名，不设置，会使用自动生成的随机id
    name: spring-boot-demo-admin-client
  boot:
    admin:
      client:
        # Spring Boot Admin 服务端地址
        url: "http://localhost:8000"
        instance:
          metadata:
            user:
              # 客户端端点信息的安全认证信息
              name: ${spring.security.user.name}
              password: ${spring.security.user.password}
  security:
    user:
      name: angel
      password: 123456
management:
  endpoint:
    health:
      # 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况
      show-details: always
  endpoints:
    web:
      base-path: /monitor
      exposure:
        # 设置端点暴露的哪些内容，默认["health","info"]，设置"*"代表暴露所有可访问的端点
        include: "*"
```
