转载于[说说强引用、软引用、弱引用、虚引用](https://zhuanlan.zhihu.com/p/142801322)
> 强引用（Strong Reference）   
> 软引用（Soft Reference）  
> 弱引用（Weak Reference）  
> 虚引用（Phantom Reference）    
  
这四种引用强度依次逐渐减弱。    
#### Java 中引入四种引用的目的是让程序自己决定对象的生命周期，JVM 是通过垃圾回收器对这四种引用做不同的处理，来实现对象生命周期的改变  
  
当关联的引用队列中有数据的时候，意味着指向的堆内存中的对象被回收。通过这种方式，JVM 允许我们在对象被销毁后，做一些我们自己想做的事情

