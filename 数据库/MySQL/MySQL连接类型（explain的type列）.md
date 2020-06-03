#### 参考于[mysql explain type连接类型示例](https://blog.csdn.net/robinson_0612/article/details/49447543)
```
type：
    连接类型
    system          表只有一行
    const           表最多只有一行匹配，通用用于主键或者唯一索引比较时
    eq_ref          每次与之前的表合并行都只在该表读取一行，这是除了system，const之外最好的一种，
                    特点是使用=，而且索引的所有部分都参与join且索引是主键或非空唯一键的索引
    ref             如果每次只匹配少数行，那就是比较好的一种，使用=或<=>，可以是左覆盖索引或非主键或非唯一键
    fulltext        全文搜索
    ref_or_null     与ref类似，但包括NULL
    index_merge     表示出现了索引合并优化(包括交集，并集以及交集之间的并集)，但不包括跨表和全文索引。
                    这个比较复杂，目前的理解是合并单表的范围索引扫描（如果成本估算比普通的range要更优的话）
    unique_subquery 在in子查询中，就是value in (select...)把形如“select unique_key_column”的子查询替换。
                    PS：所以不一定in子句中使用子查询就是低效的！
    index_subquery  同上，但把形如”select non_unique_key_column“的子查询替换
    range           常数值的范围
    index           a.当查询是索引覆盖的，即所有数据均可从索引树获取的时候（Extra中有Using Index）；
                    b.以索引顺序从索引中查找数据行的全表扫描（无 Using Index）；
                    c.如果Extra中Using Index与Using Where同时出现的话，则是利用索引查找键值的意思；
                    d.如单独出现，则是用读索引来代替读行，但不用于查找
    all             全表扫描
```
