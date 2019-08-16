### yml 文件可以注入spring属性properties，已知两种方式： 
1. `@Value `注入，配置在`字段`上  
  在指定字段上标识@Value("${name.nameLevel2}") , 这里用 $ ;  
2.`ConfigurationProperties(prefix = "name.nameLevel2")`注入 ，配置在`类`上  
  这里的前缀命名有要求：分隔符（如'-'）, 小写字母，数字，其中必须以字符开头  
两种方式均支持深层次注入
