#### [转载于 JAVA命令行工具](https://www.jianshu.com/p/87637b150026)  

#### 格式
>1. java [options] mainclass [args...]   从指定JAVA类启动  
>2. java [options] -jar jarfile [args...]  从可运行jar包启动  
#### 启动过程
>1. 启动JAVA运行时环境JRE  
>2. 加载所需的类  
>3. 调用类的main()方法  
#### 命令行各部分的含义
>1. options 由空格分隔的命令行选项  
>2. mainclass 待启动类包含包路径的类全名，其中需要含有main()方法  
>3. jarfile 待启动jar包的路径，其中需要manifest文件指明含有main()方法的启动类
>4. args 传给启动类main()方法的参数，由空格分隔  

### 命令行详解
#### 选项详解，命令行选项分为三类
>1. 标准选项。JVM必须实现的选项，实现通用的功能，例如：-version，-server -client ,, -Dproperty=value（指定一个<B>系统属性值</B>。属性和属性值都为字符形式，其中属性名不能含有空白字符，属性值如果需要空白字符，需要使用双引号"包裹）  
>2. 扩展选项。HotSpot实现常用功能的选项，其他JVM不一定实现。此类选项前缀为-X，例如： -Xmx size meomory maximum ，-Xms size memory startup  
>3. 高级选项。高级选项是开发者选项，不保证在所有JVM上被实现，并可能会改变  -XX:MaxHeapSize=size -XX:NewSize  
