
#### 参考于[Java多线程学习（八）线程池与Executor 框架](https://juejin.im/post/5b0f69e46fb9a009f41479b4)
scheduleAtFixedRate(): 创建并执行在给定的初始延迟之后，随后以给定的时间段首先启用的周期性动作; 那就是执行将在initialDelay之后开始，然后initialDelay+period ，然后是initialDelay + 2 * period ，等等。 如果任务的执行遇到异常，则后续的执行被抑制。 否则，任务将仅通过取消或终止执行人终止。 如果任务执行时间比其周期长，则后续执行可能会迟到，但不会同时执行。
scheduleWithFixedDelay() : 创建并执行在给定的初始延迟之后首先启用的定期动作，随后在一个执行的终止和下一个执行的开始之间给定的延迟。 如果任务的执行遇到异常，则后续的执行被抑制。 否则，任务将仅通过取消或终止执行终止。  
  
ScheduledThreadPoolExecutor为了实现周期性的执行任务，对ThreadPoolExecutor做了如下修改：  
1. 使用 DelayQueue 作为任务队列，是个时间优先级队列； Queue<Runnable>--> Queue<ScheduledFutureTask>  
2. 获取任务的方式不同  
3. 执行周期任务后，增加了额外的处理（计算下次执行时间，重新放回队列）  

ScheduledThreadPoolExecutor使用的任务队列DelayQueue封装了一个PriorityQueue，PriorityQueue会对队列中的任务进行排序，执行所需时间短的放在前面先被执行(ScheduledFutureTask的time变量小的先执行)，如果执行所需时间相同则先提交的任务将被先执行(ScheduledFutureTask的squenceNumber变量小的先执行)。
