#### 线程隔离
+ 就是使用各自的线程池，各自的队列，只要通过某种方法把任务提交到各自的队列即可，某个线程池阻塞，队列里的任务积压，不会影响另一个队列的存取
- 一个线程池，必须搭配一个阻塞队列，队列是异步化的关键，异步化，不能缩短响应时间，但是可以提高吞吐量  
#### Hystrix 资源隔离
1. 线程池隔离，引入额外线程的方式。对原来的web容器线程做管理控制，
2. 信号量隔离，它其实是并发总数的一个限制，并不能隔离影响，信号量说白了就是个计数器。计数器计算达到设定的阈值，直接就做异常处理，其依旧采用的Web容器的线程池

#### hystrix 线程池隔离实现
1. 其底层依旧是 JDK 的 ThreadPool 
2. ThreadPool的 Hystrix 包装 HystrixThreadPool，这是接口，定义外部操作
3. ThreadPool的 Hystrix 包装实现 HystrixThreadPoolDefault，负责实例化具体线程池，并把包装类注册到管理容器中（不是spring容器，是个 currentHashMap）
4. 具体实例化方法是 HystrixConcurrencyStrategy.getThreadPool -->
```
if (allowMaximumSizeToDivergeFromCoreSize) {
    final int dynamicMaximumSize = threadPoolProperties.maximumSize().get();
    if (dynamicCoreSize > dynamicMaximumSize) {
        logger.error("Hystrix ThreadPool configuration at startup for : " + threadPoolKey.name() + " is trying to set coreSize = " +
                dynamicCoreSize + " and maximumSize = " + dynamicMaximumSize + ".  Maximum size will be set to " +
                dynamicCoreSize + ", the coreSize value, since it must be equal to or greater than the coreSize value");
        return new ThreadPoolExecutor(dynamicCoreSize, dynamicCoreSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    } else {
        return new ThreadPoolExecutor(dynamicCoreSize, dynamicMaximumSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }
} else {
    return new ThreadPoolExecutor(dynamicCoreSize, dynamicCoreSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
}
```
5. 对外操作的是个工厂容器，HystrixThreadPool#Factory.getInstance --> new HystrixThreadPoolDefault --> HystrixConcurrencyStrategy.getThreadPool ,threadPoolKey在map中存在，就返回，否则新建并加入容器返回
6. 在 AbstractCommand 初始化中调用 initThreadPool --> Factory.getInstance
#### 即：threadPoolKey 相同的 HystrixCommand ，用的是一个线程池，不会隔离，key 不同则产生隔离
7. 重写 HystrixCommand 的 run 方法，调用 execute() 执行，会在 execute-> Observe->...-> run 中执行
参考[Hystrix执行原理](https://www.jianshu.com/p/254c9eea5c6b?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)
