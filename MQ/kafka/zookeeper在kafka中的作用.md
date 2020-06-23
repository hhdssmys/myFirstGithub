#### 转载于[zookeeper在kafka中的作用](https://zhuanlan.zhihu.com/p/41953232)  
+ kafka 在 zookeeper 上保存的目录结构  
> 1. /brokers/ids  
> 2. /brokers/topics    
> 3. /consumers/consumer-group/ids  
> 4. /consumers/consumer-group/offsets  
> 5. /consumers/consumer-group/owners    
  
![zk存储信息图](https://img-blog.csdn.net/20140923175837147?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGl6aGl0YW8=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
#### 转载于[kafka中zk的作用](https://blog.csdn.net/maoyeqiu/article/details/102715254)  
#### kafka 选举：Controller  
&emsp;&emsp;在于分布式系统中，总会有一个地方需要对全局 meta 做一个统一的维护，Kafka 的 Controller 就是充当这个角色的。Controller 作为 Kafka Server端一个重要的组件，它的角色类似于其他分布式系统 Master 的角色，跟其他系统不一样的是，Kafka集群的任何一台Broker都可以作为Controller，但是在一个集群中同时只会有一个 Controller是alive状态。<B>Broker在启动时，会尝试去ZK创建/controller节点，第一个成功创建/controller节点的Broker会被指定为为控制器</B>记录epoch选举周期（controller所在broker挂掉就会重新选举）     
#### Controller 做的主要事情如下：  
1. Broker 的上线、下线处理；  
2. 新创建的 topic 或已有 topic 的分区扩容，处理分区副本的分配、leader 选举；  
3. 管理所有副本的状态机和分区的状态机，处理状态机的变化事件；  
4. topic 删除、副本迁移、leader 切换等处理  
