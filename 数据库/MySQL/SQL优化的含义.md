#### SQL执行顺序：  
1. FROM <left_table>
2. ON <join_condition>
3. <join_type> JOIN <right_table>
4. WHERE <where_condition>
5. GROUP BY <group_by_list>
6. WITH {CUBE|ROLLUP}  &emsp;&emsp;&emsp;-- ROLLUP 分组统计汇总 | CUBE 更大维度的统计
7. HAVING <having_condition>
8. SELECT 
9. DISTINCT <select_list>
10. ORDER BY <order_by_list>
11. LIMIT <limit_number>

#### SQL优化的原则：
1. 漏斗法则：投入资源与收益的比较，性价比最高的是：减少数据的访问  
2. Do.Do: 1）尽量让数据库少做工作、甚至不做工作 2）如果数据库必须做这件事件，那么请尽快做完它，提高系统整体吞吐量  

#### SQL优化的含义：简单说：产出一个最佳的执行计划，并按照该计划执行  
> SQL的执行依赖于`优化器`产出的`执行计划`，优化器是基于成本（采用估算）选择执行计划的。  
> 第一目的：理解现有优化器选择的行为，并考虑是否是最佳行为，并基于此是否修改库表的设置  
> 第二目的：通过人工介入的方式，让数据库以更优的方式执行SQL
##   
###### 参考：[重新了解SQL](https://juejin.im/post/5d26c89df265da1b8e70c955#heading-13)  
