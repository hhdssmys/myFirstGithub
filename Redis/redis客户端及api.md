#### 转载于[Redis-API学习及Jedis源码原理分析](https://www.cnblogs.com/jing99/p/12657552.html)  
由于Redis有5中数据类型，每种数据类型的基本操作API也存在差异，同时Redis有数种部署模式，在使用时，也就存在直接访问以及集群访问两种情况，因此在调用API时也有所差别。  
1. 直接访问模式；单机、主从乃至哨兵模式时，都是直接对接点进行访问，redis命令对应于每一个jedis接口      
2. 集群访问模式；如果是Redis集群，那么Jedis在客户端初始化时将使用JedisCluster，由于Redis集群下数据是分节点存储的，因此直接访问模式下的部分Redis API将不支持  

