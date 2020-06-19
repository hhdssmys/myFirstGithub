#### 参考：    
  [芋道 Spring Security OAuth2 入门](http://www.iocoder.cn/Spring-Security/OAuth2-learning/?vip)  
  [从零开始的 Spring Security OAuth2（三）](https://www.cnkirito.moe/Spring-Security-OAuth2-3/)  
  [从零开始的 Spring Security OAuth2（二）](https://www.cnkirito.moe/Spring-Security-OAuth2-2/)  
  [芋道 Spring Boot 安全框架 Spring Security 入门](http://www.iocoder.cn/Spring-Boot/Spring-Security/?vip)  
  [从零开始的 Spring Security OAuth2](https://www.cnkirito.moe/Spring-Security-OAuth2-1/)  
#### Oauth2: 客户端 取得 用户 授权，于资源提供商的 认证服务器 申请令牌，于资源提供商的 资源服务器 验证令牌，访问受限资源  
认证服务器： 验证用户信息，生成token   
资源服务器： 分离请求中的 token ，校验 token ，访问受限资源       
#### 使用oauth2保护你的应用，可以分为简易的分为三个步骤:  
配置资源服务器  
配置认证服务器  
配置spring security  

#### security 是通过 责任链模式 遍历每一个过滤器的authenticate验证方法，一个不过就结束，每一个不同的过滤器都提供了类似的验证流程，
#### 但是 oauth—token 验证是重写了容器的顶级身份认证接口，稍显不同，至少包括两部分（token生成（身份验证通过） + token验证 ）   

#### 简单说下 spring security oauth2 的认证思路。
1. client 模式，没有用户的概念，直接与认证服务器交互，用配置中的客户端信息去申请 accessToken，客户端有自己的 client_id,client_secret 对应于用户的 username,password，而客户端也拥有自己的 authorities，当采取 client 模式认证时，对应的权限也就是客户端自己的 authorities。

2. password 模式，自己本身有一套用户体系，在认证时需要带上自己的用户名和密码，以及客户端的 client_id,client_secret。此时，accessToken 所包含的权限是用户本身的权限，而不是客户端的权限。

我对于两种模式的理解便是，如果你的系统已经有了一套用户体系，每个用户也有了一定的权限，可以采用 password 模式；如果仅仅是接口的对接，不考虑用户，则可以使用 client 模式。
