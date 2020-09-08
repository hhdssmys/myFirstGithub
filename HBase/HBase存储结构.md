#### [一文讲清HBase存储结构](https://juejin.im/post/6844903753271754759)  
table -> row -> column Family(列簇) -> Qualifier（列）-> version -> value  
HBase 是分布式，多版本，面向列的 K-V 数据库，底层依赖 HDFS  
k-v: 
| row-key | column family | column/Qualifier | timeStamp | Value | 
| ------- |      -------  |       -------    |  -------  | ----- |
| Key | Key | Key | Key |value | 

