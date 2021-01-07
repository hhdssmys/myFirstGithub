#### [转载于 ForkJoinPool 的使用以及原理](https://my.oschina.net/xinxingegeya/blog/3007257)
ForkJoinPool 不是为了替代 ExecutorService，而是它的补充，ForkJoinPool 主要用于实现“分而治之”的算法，特别是分治之后递归调用的函数，ForkJoinPool 最适合的是计算密集型的任务，如果存在 I/O，线程间同步，sleep() 等会造成线程长时间阻塞的情况时，最好配合使用 ManagedBlocker  

ForkJoinTask：我们要使用 ForkJoin 框架，必须首先创建一个 ForkJoin 任务 ForkJoinTask。它提供在任务中执行 fork() 和 join() 操作的机制，通常情况下我们不需要直接继承 ForkJoinTask 类，而只需要继承它的子类，ForkJoin 框架提供了以下两个子类：  
+ RecursiveAction：用于没有返回结果的任务。 
+ RecursiveTask ：用于有返回结果的任务  

#### ForkJoinTask 的核心重写方法是 compute，在内部完成 分治（SumTask.fork）和合并（合并如果需要，SumTask1.join + SumTask2.join）  
分治（大规模问题可以拆解为类似的小规模）：分治法的设计思想是，将一个难以直接解决的大问题，分割成一些规模较小的相同问题，以便各个击破，分而治之  
递归：直接或间接地调用自身的算法称为递归算法  
由分治法产生的子问题往往是原问题的较小模式，这就为使用递归技术提供了方便。在这种情况下，反复应用分治手段，可以使子问题与原问题类型一致而其规模却不断缩小，最终使子问题缩小到很容易直接求出其解。这自然导致递归过程的产生  
#### 分治与递归像一对孪生兄弟，经常同时应用在算法设计之中，并由此产生许多高效算法  

ForkJoinPool ：ForkJoinTask 需要通过 ForkJoinPool 来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务  

队列（FIFO），先进先出，一端存一端取，队尾存，队首取  
双端队列：两端都可以存取  
LIFO，后进先出，队尾进，队尾取

#### ForkJoinPool work stealing 算法
1. ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque，两端都可以存取），里面存放的对象是任务（ForkJoinTask）。  
2. 每个工作线程在运行中产生新的任务（通常是因为调用了 fork()）时，会放入工作队列的队尾，并且工作线程在处理自己的工作队列时，使用的是 LIFO 方式，也就是说每次从队尾取出任务来执行。  
3. 每个工作线程在处理自己的工作队列同时，会尝试窃取一个任务（或是来自于刚刚提交到 pool 的任务 ForkJoinPool.submit 的外部任务，或是来自于其他工作线程的工作队列），窃取的任务位于其他线程的工作队列的队首，也就是说工作线程在窃取其他工作线程的任务时，使用的是 FIFO 方式。
4. 在遇到 join() 时，如果需要 join 的任务尚未完成，则会先处理其他任务，并等待其完成。
5. 在既没有自己的任务，也没有可以窃取的任务时，进入休眠。

#### ForkJoinTask fork 方法
fork() 做的工作只有一件事，既是把任务推入当前工作线程的工作队列里。可以参看以下的源代码：  
```java
public final ForkJoinTask<V> fork() {
    Thread t;
    if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)
        ((ForkJoinWorkerThread)t).workQueue.push(this);
    else
        ForkJoinPool.common.externalPush(this);
    return this;
}
```    
#### ForkJoinTask join 方法
join() 的工作则复杂得多，也是 join() 可以使得线程免于被阻塞的原因——不像同名的 Thread.join()  
1. 检查调用 join() 的线程是否是 ForkJoinThread 线程。如果不是（例如 main 线程），则阻塞当前线程，等待任务完成。如果是，则不阻塞。
2. 查看任务的完成状态，如果已经完成，直接返回结果。
3. 如果任务尚未完成，但处于自己的工作队列内，则完成它。
4. 如果任务已经被其他的工作线程偷走，则窃取这个小偷的工作队列内的任务（以 FIFO 方式），执行，以期帮助它早日完成欲 join 的任务。
5. 如果偷走任务的小偷也已经把自己的任务全部做完，正在等待需要 join 的任务时，则找到小偷的小偷，帮助它完成它的任务。
6. 递归地执行第5步。

#### 每个工作线程自己拥有的工作队列 WorkQueue 以外，ForkJoinPool 自身也拥有工作队列，这些工作队列的作用是用来接收由外部线程（非 ForkJoinThread 线程）提交过来的任务，而这些工作队列被称为 submitting queue  
submit() 和 fork() 其实没有本质区别，只是提交对象变成了 submitting queue 而已（还有一些同步，初始化的操作）。submitting queue 和其他 work queue 一样，是工作线程”窃取“的对象，因此当其中的任务被一个工作线程成功窃取时，就意味着提交的任务真正开始进入执行阶段  
