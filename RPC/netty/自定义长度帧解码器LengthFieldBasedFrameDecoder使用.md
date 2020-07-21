### 转载于[LengthFieldBasedFrameDecoder 秒懂](https://www.cnblogs.com/crazymakercircle/p/10294745.html)
#### 上面用到的自定义长度解码器 LengthFieldBasedFrameDecoder 构造器，涉及7个参数，前5个都与长度域（数据包中的长度字段）相关，具体介绍如下：  
1. maxFrameLength - 发送的数据包最大长度；
2. lengthFieldOffset - 长度域偏移量，指的是长度域位于整个数据包字节数组中的下标；
3. lengthFieldLength - 长度域的自己的字节数长度。
4. lengthAdjustment – 长度域的偏移量矫正。 如果长度域的值，除了包含有效数据域的长度外，还包含了其他域（如长度域自身）长度，那么，就需要进行矫正。矫正的值为：包长 - 长度域的值 – 长度域偏移 – 长度域长。
5. initialBytesToStrip – 丢弃的起始字节数。丢弃处于有效数据前面的字节数量。比如前面有4个节点的长度域，则它的值为4。
6. failFast - true: 读取到长度域超过maxFrameLength，就抛出一个 TooLongFrameException。false: 只有真正读取完长度域的值表示的字节之后，才会抛出 TooLongFrameException，默认情况下设置为true，建议不要修改，否则可能会造成内存溢出
7. ByteOrder - 数据存储采用大端模式或小端模式  
#### 注意：第 4 个参数格外难以理解，当长度域的值不只是有效数据时，就需要修正  
#### 主要可以解决 TCP 粘包拆包  
#### 其他文章[使用LengthFieldBasedFrameDecoder解码器及自定义](https://www.jianshu.com/p/64dc7ee8c713)
