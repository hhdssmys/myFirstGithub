### 参考
### [Java集合 --- TreeMap底层实现和原理（源码解析）](https://www.jianshu.com/p/94acb78a8a4f)
### [java中的排序--排序容器_TreeSet与TreeMap](https://cloud.tencent.com/developer/article/1410681?from=information.detail.TreeSet%E6%8E%92%E5%BA%8F)
### [使用TreeMap](https://www.liaoxuefeng.com/wiki/1252599548343744/1265117109276544)
### [关于TreeMap和TreeSet的实例研究和总结](https://blog.csdn.net/siying8419/article/details/80871179)

#### TreeSet, TreeMap 的使用
1. 往TreeSet添加元素的时候，如果元素本身具备了自然顺序的特性（数字按照值得大小，字符串按照顺序字符的ＡＳＣＩＩ码排序），那么就按照元素自然顺序的特性进行排序存储。  
2. 往TreeSet添加元素的时候，如果元素本身不具备自然顺序的特性，那么该元素所属的类必须要实现Comparable接口，把元素的比较规则定义在compareTo()方法上。  
3. 如果比较元素的时候，compareTo方法返回的是0，那么该元素就被视为重复元素，不允许添加.(注意：TreeSet与HashCode、equals方法是没有任何关系。)  
4. 往TreeSet添加元素的时候, 如果元素本身没有具备自然顺序 的特性，而元素所属的类也没有实现Comparable接口，那么必须要在创建TreeSet的时候传入一个比较器。  
5. 往TreeSet添加元素的时候，如果元素本身不具备自然顺序的特性，而元素所属的类已经实现了Comparable接口，在创建TreeSet对象的时候也传入了比较器那么是以 比较器的比较规则优先 使用。
6. TreeMap 基本原则和上面的TreeSet一样，但是推荐使用在构建TreeMap的时候，尽量传入一个comparator来进行对map的排序  
7. //改变数据，排序不会改变，TreeSet是在添加数据时进行排序，数据更改不会影响原来的顺序（我认为因为 keySet != null 就不会重建），因此不能修改类中数据，否则可能重复。需要在设计类时使用final修饰字段属性，同时不提供相应set、get方法。

#### TreeMap 底层实现
1. Entry 的结构  
2. put, get, del 实现  
3. 红黑树实现  
