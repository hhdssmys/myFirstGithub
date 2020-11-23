### 转载于:
>#### [MySQL JSON类型](https://www.jianshu.com/p/25161add5e4b)
>#### [mysql根据json字段内容作为查询条件（包括json数组），检索数据](https://blog.csdn.net/qq_21187515/article/details/90760337)

#### MySQL支持JSON数据类型。相比于Json格式的字符串类型，JSON数据类型的优势有：  
>1. 存储在JSON列中的JSON文档的会被自动验证。无效的文档会产生错误；  
>2. 最佳存储格式。存储在JSON列中的JSON文档会被转换为允许快速读取文档元素的内部格式。  
#### mysql根据json字段的内容检索查询数据
>1. 使用 字段->'$.json属性'进行查询条件 eg.. field->'$.json_field'='value'  
>2. 使用json_extract函数查询，json_extract(字段,"$.json属性")
>3. 根据json数组查询，用JSON_CONTAINS(字段,JSON_OBJECT('json属性', "内容"))  
>  
><B> mysql5.7以上支持json的操作，以及增加了json存储类型</B>
