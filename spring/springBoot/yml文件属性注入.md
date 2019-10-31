### yml 文件可以注入spring属性properties，已知两种方式： 
1. `@Value `注入，配置在`字段`上  
  在指定字段上标识@Value("${name.nameLevel2}") , 这里用 $ ;  
2.`ConfigurationProperties(prefix = "name.nameLevel2")`注入 ，配置在`类`上  
  这里的前缀命名有要求：分隔符（如'-'）, 小写字母，数字，其中必须以字符开头  
  如果出现中文乱码问题，是否考虑编辑器文件保存时，两方文件编码一致，建议把所有文件编码调整至utf-8  
两种方式均支持深层次注入
