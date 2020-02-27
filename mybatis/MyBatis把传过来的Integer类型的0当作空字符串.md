传递Integer类型的值，如果传递的是0，到mybatis的mapper的xml文件中是把值当作空字符串，mybatis源码对其进行了强制定义。

```
<if test="status != null and status !=  '' or status == 0">
```

使用时增加多一个or status == 0判断

实际上，Integer类型和空字符串是不需要判断比较的。业务上一般比较是否为null就行了

```
<if test="status != null">
```
