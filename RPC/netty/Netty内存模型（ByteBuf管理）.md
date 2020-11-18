### [转载于  Netty - 内存模型原理 (ByteBuf管理)](https://juejin.im/post/6844904158772887559)
#### ByteBuf 分类
1. Netty 使用 ByteBuf 对象作为数据容器，进行 I/O 读写操作，Netty 的内存管理也是围绕着 ByteBuf 对象高效地分配和释放
>+ Unpooled，非池化内存每次分配时<B>直接调用系统 API</B> 向操作系统申请ByteBuf需要的同样大小内存，用完后通过<B>系统调用进行释放</B>
>- Pooled，池化内存分配时基于<B>预分配的一整块大内存</B>，取其中的部分封装成ByteBuf提供使用，用完后回收到内存池中  
>+ tips: Netty4默认使用Pooled的方式,可通过参数 -Dio.netty.allocator.type=unpooled 或 pooled 进行设置
>+ 
>- Heap 指ByteBuf关联的内存JVM堆内分配，分配的内存受GC 管理
>+ Direct，指ByteBuf关联的内存在JVM堆外分配，分配的内存不受GC管理，需要通过系统调用实现申请和释放，底层基于Java NIO的DirectByteBuffer对象  
>- tips: 使用堆外内存的优势在于，Java进行I/O操作时，需要传入数据所在缓冲区起始地址和长度，<B>由于GC的存在，对象在堆中的位置往往会发生移动，导致对象地址变化，系统调用出错</B>。为避免这种情况，当基于堆内存进行I/O系统调用时，<B>需要将内存拷贝到堆外</B>，而直接基于堆外内存进行I/O操作的话，可以节省该拷贝成本  

#### 算法设计  
>+ Netty 先向系统申请一整块连续内存，称为chunk，默认大小chunkSize = 16Mb，通过 PoolChunk 对象包装
>- 为了更细粒度的管理，Netty 将 chunk 进一步拆分为 page ，默认每个 chunk 包含 2048 个 page(pageSize = 8Kb)
>+ 不同大小池化内存对象的分配策略不同，根据申请内存大小分为：
>>1. 小对象内存管理：微型对象(tiny)：规整后为16的整倍数，如16、32、48、...、496，一共31种规格;小型对象(small)：规整后为2的幂的，有512、1024、2048、4096，一共4种规格 PoolSubpage     
>>2. 普通对象 (pageSize/2, chunkSize]  
>>3. 巨型对象内存管理:Netty采用的是非池化管理策略，在每次请求分配内存时单独创建特殊的非池化PoolChunk对象进行管理，内部memoryMap为null，当对象内存释放时整个Chunk内存释放   

#### 平台对象内存申请（PoolChunk 内存分配 ）
>+ 算法结构： 完全二叉树，叶子节点是 page
>- 存储标识： byte数组(memoryMap)，初始值 memoryMap[id] = depth_of_id，标识 id节点空闲；memoryMap[id] = maxDepth + 1：id节点全部已使用，depth_of_id < memoryMap[id] < maxOrder + 1：id节点部分已使用  
>+ 内存大小归一化(向上取值): normalizeCapacity = chunkSize/2^d，拿 d 和 memoryMap[id]比较
>- 算法从根节点开始遍历,需要在depth = d的层级中找到第一块空闲内存
>+ 内存释放：因为算法是从上往下开始遍历，所以在实际处理中，节点分配内存后仅更新祖先节点的值，并没有更新子节点的值；释放内存时，根据申请内存返回的id，将 memoryMap[id]更新为depth_of_id，同时设置id节点的祖先节点值为各自左右节点的最小值  

#### 弹性伸缩 PoolArena（如何管理多个chunk，构建成能够弹性伸缩内存池）
>1. PoolArena 内部持有6个 PoolChunkList，各个PoolChunkList持有的PoolChunk的使用率区间不同,按顺序依次访问q050、q025、q000、qInit、q075,这样做的好处是，使分配后各个区间内存使用率更多处于[75,100)的区间范围内，提高PoolChunk内存使用率的同时也兼顾效率，减少在PoolChunkList中PoolChunk的遍历  
>2. PoolArena内部持有2个PoolSubpage数组，分别存储tiny和small规格类型的PoolSubpage,这些小对象直接分配一个page会造成浪费，在page中进行平衡树的标记又额外消耗更多空间，因此Netty的实现是：先PoolChunk中申请空闲page，同一个page分为相同大小规格的小内存进行存储,即poolSubpage 存储大小相同的块    
 ```
// 容纳使用率 (0,25%) 的PoolChunk
private final PoolChunkList<T> qInit;
// [1%,50%) 
private final PoolChunkList<T> q000;
// [25%, 75%) 
private final PoolChunkList<T> q025;
// [50%, 100%) 
private final PoolChunkList<T> q050;
// [75%, 100%) 
private final PoolChunkList<T> q075;
// 100% 
private final PoolChunkList<T> q100;

// 数组长度32，实际使用域从index = 1开始，对应31种tiny规格PoolSubpage
private final PoolSubpage<T>[] tinySubpagePools;
// 数组长度4，对应4种small规格PoolSubpage
private final PoolSubpage<T>[] smallSubpagePools;

```

#### 并发设计  
>+ 无论是 PoolChunk 的平衡树标记或者 PoolSubpage 的 bitmap 标记都是多线程不安全
