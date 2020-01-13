1. 在 spring.factories 中注册引入的类
2. 自定义EnableXXX 注解，用 @Import(CasClientConfiguration.class) 引入自定义的 AutoConfigure 配置类
3. spring-boot-configuration-processor 的作用是编译时生成 spring-configuration-metadata.json ，此文件主要给IDE使用。如当配置此jar相关配置属性在 application.properties ，你可以用ctlr+鼠标左键点击属性名，IDE会跳转到你配置此属性的类中

[转载至 cas5.3.2单点登录-编写自己的cas-starter(九) ](https://blog.csdn.net/qq_34021712/article/details/81486699)
