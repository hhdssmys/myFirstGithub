#### 转载于[mysql聚合函数rollup和cube](https://blog.csdn.net/liuxiao723846/article/details/48970443)  
with rollup 通常和 group by 语句一起使用，是根据维度在分组的结果集中进行聚合操作。——对group by的分组进行汇总。
##### rollup注意：
1. ORDER BY不能在rollup中使用，两者为互斥关键字；  
2. 如果分组的列包含NULL值，那么rollup的结果可能不正确，因为在rollup中进行的分组统计时，null具有特殊意义。因此在进行rollup时可以先将null转换成一个不可能存在的值，或者没有特别含义的值，比如：IFNULL(xxx,0)  
3. mysql中没有像oracle那样的grouping()函数；  
