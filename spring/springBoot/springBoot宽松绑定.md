### 转载于[springBoot宽松绑定](https://www.jianshu.com/p/3a6ac189b14c)

#### 环境变量绑定
>由于操作系统对环境变量的命名都有严格的要求，如Linux shell变量，只能是字母（一般大写）、数字及下划线。  
>标准形式（ canonical-form ）转到环境变量的规则:
>>1. 使用下划线替换点号
>>2. 去掉短横线(dash -)
>>3. 转为大写  
> eg.&emsp;&emsp;&emsp;demo.test.ab-cd-ef 转为DEMO_TEST_ABCDEF
#### 绑定规则-命名风格
>1. 驼峰式（camel case,如：relaxedBinding）
>2. 短横线隔开式（ kebab case,如：relaxed-binding，推荐首选在.properties和.yml 文件使用）
>3. 下划线表示法 (underscore notation,如：relaxed_binding，备选在.properties和.yml 文件使用)
>4. 大写格式 (upper case format ,如：RELAXED_BINDING，环境变量中使用)
#### 推荐使用短横线隔开式（kebab case）,个人喜欢 点分隔
