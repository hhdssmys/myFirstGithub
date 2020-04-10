#### 装载于 [从零开始的 Spring Security OAuth2](https://www.cnkirito.moe/Spring-Security-OAuth2-1/)

#### 简单说下 spring security oauth2 的认证思路。
1. client 模式，没有用户的概念，直接与认证服务器交互，用配置中的客户端信息去申请 accessToken，客户端有自己的 client_id,client_secret 对应于用户的 username,password，而客户端也拥有自己的 authorities，当采取 client 模式认证时，对应的权限也就是客户端自己的 authorities。

2. password 模式，自己本身有一套用户体系，在认证时需要带上自己的用户名和密码，以及客户端的 client_id,client_secret。此时，accessToken 所包含的权限是用户本身的权限，而不是客户端的权限。

我对于两种模式的理解便是，如果你的系统已经有了一套用户体系，每个用户也有了一定的权限，可以采用 password 模式；如果仅仅是接口的对接，不考虑用户，则可以使用 client 模式。
