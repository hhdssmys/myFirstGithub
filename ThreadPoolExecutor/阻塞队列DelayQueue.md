#### 参考于[精巧好用的DelayQueue](https://www.cnblogs.com/jobs/archive/2007/04/27/730255.html)  
DelayQueue内部是使用PriorityQueue实现的：  
`DelayQueue = BlockingQueue + PriorityQueue + Delayed`
```
public interface Comparable<T> {
    public int compareTo(T o);
}
public interface Delayed extends Comparable<Delayed> {
    long getDelay(TimeUnit unit);//返回一个固定值  
}
public class DelayQueue<E extends Delayed> implements BlockingQueue<E> { 
    private final PriorityQueue<E> q = new PriorityQueue<E>(); //优先队列的元是 Delayed 比较接口，计算出执行时间，并以此为比较因子，执行时间早的在前    
}
```
元素进入队列后，先进行排序，然后，<B>只有getDelay也就是剩余时间为0的时候(即延迟期满)</B>，该元素才有资格被消费者从队列中取出来，所以构造函数一般都有一个时间传入  
参考[队列之DelayQueue用法](https://www.jianshu.com/p/bf9f6b08ba5b)  
#### 注：
1. 阻塞队列的基本方法 offer ，take  
2. 优先队列是最小堆（完全二叉树）实现的，其底层结构可以是数组  

 
