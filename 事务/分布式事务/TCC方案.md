#### &emsp;&emsp;分布式事务只要保证数据的最终一致性即可，try 保证成功，代表资源锁住，不论 confirm 成功与否，都不会影响该业务数据的一致性，即使失败（服务宕机等）也可以根据日志等，重用 try 锁住的资源，直至成功。    
&emsp;&emsp;CAP 理论中，P 分区容错性一定不可避免，因此通常牺牲 C 一致性，来获取 A 可用性。  
&emsp;&emsp;BASE理论是对CAP中的一致性和可用性进行一个权衡的结果，理论的核心思想就是：我们无法做到强一致，但每个应用都可以根据自身的业务特点，采用适当的方式来使系统达到最终一致性  
Basically Available（基本可用）  
Soft state（软状态）  
Eventually consistent（最终一致性）  
#### XA两阶段提交资源层面的，而TCC实际上把资源层面二阶段提交上提到了业务层面来实现。有效了的避免了XA两阶段提交占用资源锁时间过长导致的性能地下问题  
##### 参考文章：
[分布式事务 TCC 两阶段补偿](https://juejin.im/post/5d9eef126fb9a04e2357684a)  
[拜托，面试请不要再问我TCC分布式事务的实现原理](https://juejin.im/post/5bf201f7f265da610f63528a)  
[基于可靠消息方案的分布式事务：Lottor介绍](http://blueskykong.com/2018/05/04/lottor-intro/)  
