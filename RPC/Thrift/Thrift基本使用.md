### [Thrift基本使用](https://matt33.com/2016/04/07/thrift-learn/)
### 目录：
1. 安装  
2. Thrift Type  
3. 数据传输格式（协议），传输方式（阻塞，非阻塞），服务模型（单线程阻塞，多线程阻塞等）  
4. 使用：编译 IDL 后，RPC服务提供：具体实现+服务暴露server；RPC服务消费：客户端 client 调用  
<hr />   
  
###  [IDL官网地址](http://thrift.apache.org/docs/idl)   
  
### Thrift Type  
1. 基本类型（Base Types）  
 >+ bool: 布尔变量（A boolean value, one byte）；  
 >+ byte: 8位有符号整数（A signed byte）；  
 >+ i16: 16位有符号整数（A 16-bit signed integer）；  
 >+ i32: 32位有符号整数（A 32-bit signed integer）；  
 >+ i64: 64位有符号整数（A 64-bit signed integer）；  
 >+ double: 64位浮点数（A 64-bit floating point number）；  
 >+ binary: byte数组（A byte array）；  
 >+ string: 字符串（Encoding agnostic text or binary string）   
 > #### Thrift 不支持无符号整数，因为有些语言也不支持无符号整数，比如Java
2. 容器类型（Containers）   
 >+ list: 一系列由T类型的数据组成的有序列表，元素可以重复；
 >+ set: 一系列由T类型的数据组成的无序集合，元素不可重复
 >+ map: 一个字典结构，key为T1类型，value为T2类型；
 > #### 这些集合中的元素可以是除了服务的任何Thrift类型（包括结构体和异常）。
3. 结构体（Struct） 
 > 结构体中包含一系列的强类型域，等同于无继承的class。可以看出struct写法很类似C语言的结构体  
 ```
 struct Example {
  1:i32 number=10,
  2:i64 bigNumber,
  3:list<double> decimals,
  4:string name="thrifty"
}
 ```
4. 联合(Union)
 > 在一个结构体中，如果field之间的关系是互斥的，即只能有一个field被使用被赋值。在这种情况下，我们可以使用union来声明这个结构体，而不是一堆堆optional的field，语意上也更明确了。例如：  
 ```
 union JavaObjectArg {
  1: i32 int_arg;
  2: i64 long_arg;
  3: string string_arg;
  4: bool bool_arg;
  5: binary binary_arg;
  6: double double_arg;
}
 ```
5. 异常(Exceptions)
 > 可以自定义异常类型，所定义的异常会继承对应语言的异常基类，例如java，就会继承 java.lang.Exception.   
 ```
 exception InvalidOperation {
  1: i32 what,
  2: string why
 }
 ```
6. 服务(service)  
 > Thrift定义服务相当于Java中创建Interface一样，创建的service经过代码生成命令之后就会生成客户端和服务端的框架代码。定义形式如下：
 ```
 service Hello{
  string helloString(1:string para)
  i32 helloInt(1:i32 para)
  bool helloBoolean(1:bool para)
  void helloVoid()
  string helloNull()
}
 ```
8. 命名空间(namespace)
 > Thrift的命名空间相当于Java中的package的意思，主要目的是组织代码。thrift使用关键字namespace定义命名空间，如：
 ```
 namespace java service.demo
 ```
 > 注意末尾不能有分号，由此生成的代码，其包路径结构为service.demo.
9. 可选与必选
 > Thrift提供两个关键字required，optional，分别用于表示对应的字段时必填的还是可选的。例如： 
 ```
 struct People {
    1: required string name;
    2: optional i32 age;
 }
 ```
 > 表示name是必填的，age是可选的。
 
### Thrift支持的数据传输格式、数据传输方式和服务模型
#### 协议（传输格式）
Thrift可以让用户选择客户端与服务端之间传输通信协议的类别，在传输协议上<B>总体划分为文本(text)和二进制(binary)传输协议</B>。为节约带宽，提高传输效率，一般情况下使用二进制类型的传输协议为多数，有时还会使用基于文本类型的协议，这需要根据项目/产品中的实际需求。常用协议有以下几种：  
 >+ TBinaryProtocol： 二进制格式；
 >+ TCompactProtocol：高效率的、密集的二进制编码格式进行数据传输；
 >+ TJSONProtocol：JSON文本格式；
 >+ TSimpleJSONProtocol：提供JSON只写协议, 生成的文件很容易通过脚本语言解析；
 >+ TDebugProtocol：使用易懂的可读的文本格式，以便于debug。
#### 传输层（数据传输方式）
 >+ TSocket：阻塞式socker；
 >+ TFramedTransport：使用非阻塞方式，以frame为单位进行传输。
 >+ TFileTransport：以文件形式进行传输。
 >+ TMemoryTransport：将内存用于I/O. java实现时内部实际使用了简单的ByteArrayOutputStream。
 >+ TZlibTransport：使用zlib进行压缩， 与其他传输方式联合使用。当前无java实现。
 >+ TNonblockingTransport —— 使用非阻塞方式，用于构建异步客户端
#### 服务模型 （参考于 [ Thrift 网络服务模型](https://juejin.im/post/5b290e225188252d9548fe15#heading-2) ）
 >+ TSimpleServer：单线程 服务器端使用标准的 阻塞式 I/O，简单的单线程服务模型，常用于测试；【 BIO 单线程 一个线程所有请求 】
 >+ TThreadPoolServer：多线程 服务模型，使用标准的 阻塞式IO；【 BIO 多线程 一个线程处理监听 线程池处理业务 】
 >+ TNonblockingServer：单线程 服务模型，使用 非阻塞式IO（需使用TFramedTransport数据传输方式）。【 NIO 单线程 IO多路复用 一个线程处理已注册事件 】
 >+ THsHaServer（继承于TNonblockingServer）：半同步半异步 服务器端，基于 非阻塞式IO 读写和多线程工作任务处理（THsHaServer和TNonblockingServer一样，要求底层的传输通道必须使用TFramedTransport）【 NIO 多线程 一个线程取出注册事件 线程池处理提交的注册事件 】
 >+ TThreadedSelectorServer：多线程 选择器服务器端，对 THsHaServer在 异步IO 模型上进行增强（将selector中的读写IO事件(read/write)从主线程中分离出来。同时引入worker工作线程池）【 NIO 多线程 一个监听线程 多个读写线程 一个匹配线程，匹配监听Socket与读写线程 工作线程池 】

### [Thrift 概述与入门](https://juejin.im/post/5b290dbf6fb9a00e5c5f7aaa)
 
### 分层  
Thrift软件栈分层从下向上分别为：传输层(Transport Layer)、协议层(Protocol Layer)、处理层(Processor Layer)和服务层(Server Layer)。  
1. 传输层(Transport Layer)：传输层负责直接从网络中读取和写入数据，它定义了具体的网络传输协议；比如说TCP/IP传输等。
2. 协议层(Protocol Layer)：协议层定义了数据传输格式，负责网络传输数据的序列化和反序列化；比如说JSON、XML、二进制数据等。
3. 处理层(Processor Layer)：处理层是由具体的IDL（接口描述语言）生成的，封装了具体的底层网络传输和序列化方式，并委托给用户实现的Handler进行处理。
4. 服务层(Server Layer)：整合上述组件，提供具体的网络线程/IO服务模型，形成最终的服务。  

### 编译后文件  
对于开发人员而言，使用原生的Thrift框架，仅需要关注以下四个核心内部接口/类：Iface, AsyncIface, Client和AsyncClient。  
1. Iface：服务端通过实现HelloWorldService.Iface接口，向客户端的提供具体的同步业务逻辑。
2. AsyncIface：服务端通过实现HelloWorldService.Iface接口，向客户端的提供具体的异步业务逻辑。
3. Client：客户端通过HelloWorldService.Client的实例对象，以同步的方式访问服务端提供的服务方法。
4. AsyncClient：客户端通过HelloWorldService.AsyncClient的实例对象，以异步的方式访问服务端提供的服务方法。

 
 
 
 
 
 
 
 
 
