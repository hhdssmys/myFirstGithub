#### SQL优化的含义：简单说：产出一个最佳的执行计划，并按照该计划执行  
#### SQL的执行依赖于优化器产出的执行计划，优化器是基于成本（采用估算）选择执行计划的。  
>第一目的：理解现有优化器选择的行为，并考虑是否是最佳行为，并基于此是否修改库表的设置  
>第二目的：通过人工介入的方式，让数据库以更优的方式执行SQL  
#### SQL语句优化
1. 定位慢SQL： 慢查询日志   
---  记录未使用索引的查询   
mysql> set global log_queries_not_using_indexes=on;    
---  开启慢查询日志  
mysql> set global slow_query_log=on;  
---  查看慢查日志在什么地方 
mysql> show variables like 'slow_query_log_file%';  
2. 通过 explain 查询和分析SQL的执行计划  
explain select customer_id,first_name,last_name from customer;  
返回结构：type 这是最重要的列，返回连接类型   
      ref 显示索引的哪一列被使用了  
      rows 返回请求的行数  
      extra 需要额外注意 Using filesort ,Using temporary  
