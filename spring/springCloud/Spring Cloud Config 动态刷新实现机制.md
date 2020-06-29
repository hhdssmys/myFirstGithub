#### 参考：
[SpringCloud Config(配置中心)实现配置自动刷新总结](https://blog.csdn.net/wtdm_160604/article/details/83720391)  
[Spring Cloud Config 动态刷新实现机制](https://blog.csdn.net/woshilijiuyi/article/details/88293782)  
1. config 基本架构
```
git仓库 --> ConfigServer 配置中心服务端 --> ConfigClient 配置客户端，集成在应用服务  
                本地保存一份配置                拉取配置
```
2. 当远端git仓库配置文件发生改变，ConfigServer如何通知到ConfigClient端，即ConfigClient如何感知到配置发生更新？  
&emsp;&emsp;Spring Cloud Config 动态刷新需要依赖 Spring Cloud Bus，Spring Cloud Bus会向外提供一个http接口，即图中的/bus/refresh。我们将这个接口配置到远程的git的webhook上，当git上的文件内容发生变动时，就会自动调用/bus-refresh接口。Bus就会通知config-server，config-server会发布更新消息到消息总线的消息队列中，其他服务订阅到该消息就会信息刷新，从而实现整个微服务进行自动刷新。
##### 自动刷新只能刷新 @RefreshScope 注解下的配置，一些特殊配置，如数据库等，需要同样先设置数据库链接ConfigServer类，然后通过加 @RefreshScope 注解方式  
