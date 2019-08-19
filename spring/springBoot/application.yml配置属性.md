`application.yml` 配置 ，缩进替换层次配置
```
server:  
  port: 8080
  servlet:
    context-path: /propertiesDemo
spring:
  profiles:
    active: dev  #[ 配置激活环境 application-dev.yml , 前后对应即可，名字随意 ]
# spring 2.X 不能通过 management.security.enabled=false 来关闭验证登录，  
# 可以通过移除 security 依赖配置，或者重写WebSecurityConfigurerAdapter.configure()取消验证 
# 若要访问端点信息，需要配置用户名和密码
spring:
  security:
    user:
      name: angel
      password: 123456
management:
  # 端点信息接口使用的端口，为了和主系统接口使用的端口进行分离，不配置的话就是项目路径+actuator/端点路径,如/propertiesDemo/actuator/info
  server:
    port: 8090
    servlet:
      context-path: /sys
  # 单个端点的配置需要在 endpoint 下设置
  # 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况
  endpoint:
    health:
      show-details: always
  # 设置端点暴露的哪些内容，默认["health","info"]，设置"*"代表暴露所有可访问的端点
  endpoints:
    web:
#      base-path: /monitor  # 这里重写默认的端点根路径/actuator
      exposure:
        include: '*' #可以暴露指定的端点集，用‘,’分割，如：beans,env,info
```
