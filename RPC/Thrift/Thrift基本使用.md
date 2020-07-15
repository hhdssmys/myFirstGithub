### [Thrift基本使用](https://matt33.com/2016/04/07/thrift-learn/)
### 目录：
1. 安装  
2. Thrift Type  
3. 数据传输格式（协议），传输方式（阻塞，非阻塞），服务模型（单线程阻塞，多线程阻塞等）  
4. 使用：编译 IDL 后，RPC服务提供：具体实现+服务暴露server；RPC服务消费：客户端 client 调用  
<hr />  
  
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
 >+ TBinaryProtocol： 二进制格式；
 >+ TCompactProtocol：高效率的、密集的二进制编码格式进行数据传输；
 >+ TJSONProtocol：JSON格式；
 >+ TSimpleJSONProtocol：提供JSON只写协议, 生成的文件很容易通过脚本语言解析；
 >+ TDebugProtocol：使用易懂的可读的文本格式，以便于debug。
#### 传输层（数据传输方式）
 >+ TSocket：阻塞式socker；
 >+ TFramedTransport：使用非阻塞方式，以frame为单位进行传输。
 >+ TFileTransport：以文件形式进行传输。
 >+ TMemoryTransport：将内存用于I/O. java实现时内部实际使用了简单的ByteArrayOutputStream。
 >+ TZlibTransport：使用zlib进行压缩， 与其他传输方式联合使用。当前无java实现。
 >+ TNonblockingTransport —— 使用非阻塞方式，用于构建异步客户端
#### 服务模型
 >+ TSimpleServer：单线程服务器端使用标准的阻塞式 I/O，简单的单线程服务模型，常用于测试；
 >+ TThreadPoolServer：多线程服务模型，使用标准的阻塞式IO；
 >+ TNonblockingServer：多线程服务模型，使用非阻塞式IO（需使用TFramedTransport数据传输方式）。
 
 
 
 
 
 
 
 
 
 
 
