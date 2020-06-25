#### 参考于[ArrayBlockingQueue和LinkedBlockingQueue实现原理详解](https://juejin.im/post/5aeebdb26fb9a07aa83ea17e)  
#### ArrayBlockingQueue 与 LinkedBlockingQueue 的比较
相同点：ArrayBlockingQueue 和 LinkedBlockingQueue 都是通过 <B>condition 通知机制</B>来实现可阻塞式插入和删除元素，并<B>满足线程安全</B>的特性；  
不同点：1. ArrayBlockingQueue 底层是采用的数组进行实现，而 LinkedBlockingQueue 则是采用链表数据结构；  
#### 存取效率：LinkedBlockingQueue 两个锁对象，效率更高，因为 offer take 操作不会同时操作一个对象，所以两个资源锁可以
ArrayBlockingQueue 插入和删除数据，只采用了一个 lock，而 LinkedBlockingQueue 则是在插入和删除分别采用了putLock和takeLock，这样可以降低线程由于线程无法获取到 lock 而进入 WAITING 状态的可能性，从而提高了线程并发执行的效率  
