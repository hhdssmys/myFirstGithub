#### 自定义 start 的目的是为了自动装载，所以要知道自动装载的原理  
@EnableAutoConfiguration：开启自动配置功能，这个注解可以帮助我们自动载入应用程序所需要的所有默认配置  
@Import({AutoConfigurationImportSelector.class})：给IOC容器导入组件：  
1. 启动的时候会扫描所有jar路径下的META-INF/spring.factories，将其文件包装成Properties对象  
2. 从Properties对象获取到key值为EnableAutoConfiguration(factories文件的key值)的数据，然后添加到容器里边  
3. 条件化加载factories文件的value值所对应的 Configuration 类（xxxxAutoConfigurartion）中的 Bean  

#### 所以关键步骤就是： 定义 xxxxAutoConfigurartion  条件化装载 Bean ，然后在 META-INF/spring.factories 中声明EnableAutoConfiguration=xxxxAutoConfigurartion：  
自定义 start 的实现依赖  
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
    <version>x.x.x.RELEASE</version>
</dependency>
```
#### 示例 [SpringBoot自定义starter及自动配置](https://juejin.im/post/5dc375d75188255f9a287d34)
#### 示例  
1. 在 spring.factories 中注册引入的类
2. 自定义EnableXXX 注解，用 @Import(CasClientConfiguration.class) 引入自定义的 AutoConfigure 配置类
3. spring-boot-configuration-processor 的作用是编译时生成 spring-configuration-metadata.json ，此文件主要给IDE使用。如当配置此jar相关配置属性在 application.properties ，你可以用ctlr+鼠标左键点击属性名，IDE会跳转到你配置此属性的类中

[转载至 cas5.3.2单点登录-编写自己的cas-starter(九) ](https://blog.csdn.net/qq_34021712/article/details/81486699)
