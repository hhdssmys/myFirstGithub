#### 转载于[Kafka文件存储机制那些事](https://tech.meituan.com/2015/01/13/kafka-fs-design-theory.html)  
1. 同一个topic下有多个不同partition，<B>每个partition为一个目录</B>，partiton命名规则为topic名称+有序序号，第一个partiton序号从0开始，序号最大值为partitions数量减1  
2. 每个partion(目录)相当于一个巨型文件被平均分配到多个大小相等<B>segment(段)数据文件</B>中。但每个段segment file消息数量不一定相等，这种特性方便old segment file快速被删除  
3. segment file组成：由2大部分组成，分别为index file和data file，此2个文件一一对应，<B>成对出现</B>，后缀”.index”和“.log”分别表示为segment索引文件、数据文件  
4. segment文件命名规则：partion全局的第一个segment从0开始，后续<B>每个segment文件名为</B>上一个segment文件<B>最后一条消息的offset值</B>。数值最大为64位long大小，19位数字字符长度，没有数字用0填充  
5. 索引文件存储大量元数据，数据文件存储大量消息，索引文件中元数据指向对应数据文件中message的物理偏移地址。 其中以索引文件中元数据3,497为例，依次在数据文件中表示<B>第3个</B>message(在全局partiton表示第368772个message)、以及<B>该消息的物理偏移地址</B>为497


<hr />  

#### 在partition中如何通过offset查找message?
1. 只要根据offset **二分查找**文件列表，就可以快速定位到具体文件 index segment file    
2. 通过segment file查找 message 物理偏移地址，再通过 XXXX.log 顺序查找直到 offset=368776 为止  

#### 文件刷盘: Kafka运行时很少有大量读磁盘的操作，主要是定期批量写磁盘操作   
+ 写message
> 1. 消息从java堆转入page cache(即物理内存)。  
> 2. 由<B>异步线程刷盘</B>,消息从page cache刷入磁盘。  
+ 读message
> 1. 消息直接从page cache转入socket发送出去。  
> 2. 当从page cache没有找到相应数据时，此时会产生磁盘IO,从磁 盘Load消息到page cache,然后直接从socket发2去  
