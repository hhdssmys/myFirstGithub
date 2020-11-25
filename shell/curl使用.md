#### 参考[阮一峰 curl 使用指南](http://www.ruanyifeng.com/blog/2019/09/curl-reference.html)
#### curl: 
>+ -X  http动词，默认get，取值 GET | POST | DELETE
>+ -x  参数指定 HTTP 请求的代理
>+ -d 'data form'  json | key=value&key=value , 默认是post，-G 修改为Get，-d传递的就是请求参数字符串
>+ -H 'header:value'
>+ -c cookiefile 保存cookie文件
>+ -b cookieFile 以该cookie请求
>+ -F 'file=@localFileName;filename=remoteName;type=MIME_TYPE'
>+ -i 返回响应的标头，header
>+ -I 发出 HEAD 请求，只打印响应的标头  
>+  -L HTTP请求跟随服务器的重定向。curl 默认不跟随重定向
>+  --limit-rate 用来限制 HTTP 请求和回应的带宽，模拟慢网速的环境
>+  -o localFile 参数将服务器的回应保存成文件，等同于wget命令
>+  -u 'user:password' 参数用来设置服务器认证的用户名和密码
>+  -v 参数输出通信的整个过程，用于调试   
>+  --trace 参数也可以用于调试，还会输出原始的二进制数据
