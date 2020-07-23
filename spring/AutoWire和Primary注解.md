### 参考于[spring @Primary-在spring中的使用](https://blog.csdn.net/qq_16055765/article/details/78833260)

#### `@Autowired:` 在使用该注解时，
1. 首先在容器中查询对应<B>类型</B>的 bean  
>+ 如果查询结果刚好为一个，就将该bean装配给@Autowired指定的数据
>+ 如果查询的结果不止一个，那么`@Autowired`会报错，不知道注入哪个
2. 如果查询的结果为空，那么会抛出异常。解决方法时，使用required=false
#### `@Primay`告诉spring 在存在多个同类型的组件时优先选择哪一个具体的实现，其标注在具体实现上  
