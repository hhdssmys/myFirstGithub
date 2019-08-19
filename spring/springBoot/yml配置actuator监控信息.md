### 基于 spring 2.x

在1.5.x版本中通过 management.security.enabled=false 来取消安全验证  
但是在 2.0 中移除了 Security 内部类,该配置失效

#### spring 2.0 中默认端点路基为： 项目路径 + actuator + 具体端点路径，其中`项目路径`与 `actuator` 均可配置重写

```
# 若要访问端点信息，需要配置用户名和密码
spring:
  security:
    user:
      name: angel
      password: 123456
management:
  # 端点信息接口使用的端口，为了和主系统接口使用的端口进行分离
  server:
    port: 8090
    servlet:
      context-path: /sys
  # 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况
  endpoint:
    health:
      show-details: always
  # 设置端点暴露的哪些内容，默认["health","info"]，设置"*"代表暴露所有可访问的端点
  endpoints:
    web:
#      base-path: /monitor
      exposure:
        include: '*' #可以暴露指定的端点集，用‘,’分割
  ```

### 端点配置中：  
+ 可以与项目端口分离，重写默认的项目路径：
```
 server:
    port: 8090
    servlet:
      context-path: /sys
```
- 可以重写默认的 actuator:
```
endpoints:
    web:
      base-path: /monitor
```
+ endpoints 配置暴露的端点信息为：
```
endpoints:
    web:
#      base-path: /monitor
      exposure:
        include: '*' #可以暴露指定的端点集，用‘,’分割,如beans,info,env
```
- endpoint 单个端点的详细配置：
```
# 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况 
  endpoint:
    health:
      show-details: always
```
#### 正常情况下，我们并不想让所有人都可以查看端口的配置信息，故需要加 security 模块加以拦截(必须加载该模块的依赖)，如： 
```
spring:
  security:
    user:
      name: angel
      password: 123456
```
#### 可是，简单加载配置会默认拦截该模块的所有请求，这也不是我们想要的。我们需要的是 actuator 拦截，其他客户请求放开，这需要我们做定制开发，如下：  
```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置一： 配置对actuator监控所用的访问全部需要认证
        String[] securityRequest = {"/actuator","/actuator/*"};
        http.formLogin().and().authorizeRequests().antMatchers(securityRequest).authenticated();

        //配置二： 配置取消登录验证
//        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
    }
}
```
这里的配置一：拦截了 actuator 请求，放开了其他，但是你不要自定义该种模式的请，不然也会被拦截。
配置二：会取消所有的认证














