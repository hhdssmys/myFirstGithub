### [转载于 深入解析SpringBoot java-jar命令行启动原理](https://blog.csdn.net/kangjnghang/article/details/107046925)
#### 依赖：
```
<build>
    <finalName>springboot-demo</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
#### 创建完工程后，执行maven的打包命令，会生成两个jar文件：
```
springboot-demo.jar
springboot-demo.jar.original
```
> springboot-demo.jar.original 是默认的 maven-jar-plugin 生成的包。  
> springboot-demo.jar 是 spring boot maven 插件生成的jar包，里面包含了<B>应用的依赖</B>，以及spring boot相关的类，称之为 executable jar (可执行jar)或者 fat jar  
#### Executable Jar 文件结构
> Root  
> &emsp;|-- BOOT-INF  
> &emsp;|&emsp;&emsp;&emsp;|-- classes  用户自定义的类及配置文件，包含用户的启动类  
> &emsp;|&emsp;&emsp;&emsp;|-- lib      依赖 jar  
> &emsp;|-- META-INF  
> &emsp;|&emsp;&emsp;&emsp;|-- MANIFSET.MF  描述文件  
> &emsp;|&emsp;&emsp;&emsp;|-- maven  maven 的构建信息  
> &emsp;|-- org  
> &emsp;|&emsp;&emsp;&emsp;|-- springframework.boot.loader 是 Spring boot loader 相关类  
> java -jar 命令引导的具体启动类必须配置在清单文件 MANIFEST.MF 的 Main-Class 属性中  
> 可以看到Main-Class是org.springframework.boot.loader.JarLauncher ，说明项目的启动入口并不是我们自己定义的启动类，而是JarLauncher。而我们自己的项目引导类com.example.spring.boot.demo.SpringBootDemo，定义在了Start-Class属性中，这个属性并不是Java标准的MANIFEST.MF文件属性  
> spring-boot-maven-plugin 把 loader 相关类，在 maven package 周期时执行目标 repackage 过程，把他们打包进 jar 包  

##### jar 是一种类似 zip 的文件格式，可以解压，命令是： unzip jarFile -d targetPath  
