#### 转载于[github或者gitlab配置SSH指南](https://segmentfault.com/a/1190000015337773) 
1. 首先打开本地控制台，输入 ` ssh-keygen -t rsa -C "[your_mail]" -f ~/.ssh/[custom_name] `
> your_mail  表示你的邮箱地址  
> custom_name  表示公钥私钥的名称。[-f ~/.ssh/xxx]可选，默认名称为 id_rsa <B>（因为不知道根据什么读取，所以不要更改默认文件名，不然会失败，由于取默认，就只能配置一个了）</B>  
2. 这时候，可在` ~ssh/ ` 下，找到名为 `id_rsa` 和 `id_rsa.pub` 两个文件，其中 `id_rsa` 为私钥，`id_rsa.pub` 为公钥
3. 执行 `cat ~/.ssh/id_rsa.pub`，复制其内容
4. 打开gitlab，找到`头像 -> settings -> SSH Keys`，然后将`id_rsa.pub`的内容复制到 `key` 字段中
5. title 随便写，最后 add 即可
6. 测试是否成功 ssh -T 地址，如 ssh -T git@github.com， ssh -T git@someWeb
