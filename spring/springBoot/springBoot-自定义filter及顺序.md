### 参考于
### [Spring Boot 之 Filter顺序配置 @Order无效原因解读（源码理解）](https://blog.csdn.net/ieen_csdn/article/details/86612492?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.control&dist_request_id=1328655.12288.16158920501014687&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.control)

### Spring Filter 继承结构
> Filter  
>> CompositeFilter(I)  
>> GenericFilterBean(I)  
>>> DelegatingFilterProxy(C)  
>>> OncePerRequestFilter(C)   
  
SpringBoot 自带的 Filter，实现Order接口，继承下面的类，重写 OrderedXXX  ，自带Filter的顺序由 order 从小到大排序，用户也可以继承OncePerRequestFilter，Order ，插入 SpringBoot 的这些顺序中  
> OncePerRequestFilter  
>> CharacterEncodingFilter  
>> HiddenHttpMethodFilter  
>> HttpPutFormContentFilter  
>> RequestContextFilter  
>> ShallowEtagHeaderFilter  
>> AbstractRequestLoggingFilter  
  
OncePerRequestFilter：顾名思义，它能够确保在一次请求中只通过一次filter，而需要重复的执行。大家常识上都认为，一次请求本来就只filter一次，为什么还要由此特别限定呢？此方法是为了兼容不同的web container，也就是说并不是所有的container都入我们期望的只过滤一次，servlet版本不同，执行过程也不同，因此此处我有个建议：我们若是在Spring环境下使用Filter的话，个人建议继承OncePerRequestFilter吧，而不是直接实现Filter接口。这是一个比较稳妥的选择    
### 参考于  
### [从OncePerRequestFilter的源码解读去了解Spring内置的Filter的特别之处以及常见过滤器使用介绍](https://cloud.tencent.com/developer/article/1497822)  
