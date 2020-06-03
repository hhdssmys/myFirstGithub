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
      
#### SQL优化技巧  
1. 应尽量避免在 where 子句中使用!=或<>操作符，否则将引擎放弃使用索引而进行全表扫描（<B>尽量使用索引</B>）
2. 对查询进行优化，应尽量<B>避免全表扫描</B>，首先应考虑在 where 及 order by 涉及的列上建立索引（order by 索引列可以不必文件内存排序）  
3. 应尽量避免在 where 子句中对字段进行 null 值判断，否则将导致引擎放弃使用索引而进行全表扫描，如： （<B>列一般非空更好，省去一些判断，避免全表扫描</B>） 
      >select id from t where num is null  
      >可以在num上设置默认值0，确保表中num列没有null值，然后这样查询：  
      >select id from t where num=0  
4. 尽量避免在 where 子句中使用 or 来连接条件，否则将导致引擎放弃使用索引而进行全表扫描 ，如：（避免 OR 产生全表扫描）    
      >select id from t where num=10 or num=20  
      >可以这样查询：  
      >select id from t where num=10  
      >union all  
      >select id from t where num=20  
5. 避免大事务操作，减少共享资源的竞争度，提高并发能力  
#### 参考[MySQL SQL语句优化技巧](http://www.uml.org.cn/sjjm/201610184.asp)
