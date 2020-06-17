#### 转载于 [Spring Security(一)--Architecture Overview](https://www.cnkirito.moe/spring-security-1/)
#### security ：身份验证 + 角色授权
其中验证（用户信息对比）主要涉及的类为：  
1. AuthenticationManager#authenticate() --调用--> ProviderManager#authenticate()  
AuthenticationManager的实现类一般是ProviderManager  
2. ProviderManager#authenticate()  --调用--> AuthenticationProvider#authenticate()  
ProviderManager内部维护了一个List,真正的身份认证是由一系列AuthenticationProvider去完成  
3. AuthenticationProvider#authenticate()  --调用--> AbstractUserDetailsAuthenticationProvider#authenticate()  --调用--> DaoAuthenticationProvider#retrieveUser()
AuthenticationProvider的常用实现类则是DaoAuthenticationProvider
4. DaoAuthenticationProvider#retrieveUser() --调用-->  UserDetailsService#loadUserByUsername()  
DaoAuthenticationProvider内部又聚合了一个UserDetailsService接口，<B>UserDetailsService才是获取用户详细信息的最终接口</B>  

&emsp;&emsp;使用容器中的顶级身份管理器AuthenticationManager去进行身份认证（AuthenticationManager的实现类一般是ProviderManager。而ProviderManager内部维护了一个List,真正的身份认证是由一系列AuthenticationProvider去完成。而AuthenticationProvider的常用实现类则是DaoAuthenticationProvider，DaoAuthenticationProvider内部又聚合了一个UserDetailsService接口，UserDetailsService才是获取用户详细信息的最终接口


