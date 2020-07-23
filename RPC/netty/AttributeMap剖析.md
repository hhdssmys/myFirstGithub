### 参考于[AttributeMap剖析](https://zhuanlan.zhihu.com/p/89933318)
AttributeMap可以看成是一个key为AttributeKey类型，value为Attribute类型的Map,通过 AttributeKey的id做一个划分，来做个分段锁。使用数组+链表作结构(类似于1.7以前的 HashMap)    
Attribute内存储的是一个值引用，它可以原子的更新内容，是线程安全的（类似于单个节点，包含key-value）  
AttributeKey是基于ConstantPool进行缓存的。new AttributeKey<Object>(id, name);  
#### 其他参考[AttributeKey、AttributeMap、Attribute](https://www.jianshu.com/p/e7d9a2e8c0ac)  
[Netty中ChannelOption和AttributeKey分析](https://blog.csdn.net/summerZBH123/article/details/80035126)
