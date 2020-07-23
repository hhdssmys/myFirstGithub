&emsp;&emsp;本文从 backlog 参数出发，详细分析了 TCP 三次握手过程，以及在握手过程中涉及到的两个队列：半连接队列和全连接队列，当完成第一次握手后，会将连接放入到半连接队列中，当完成第三次握手后，会将连接放入到全连接队列中，当调用操作系统的 accept()函数时，会将连接从全连接队列中取出，从而创建一个客户端套接字，供后面的数据读写。  
&emsp;&emsp;其中半连接队列的大小等于 max(64,tcp_max_syn_backlog)，tcp_max_syn_backlog 是一个内核参数。全连接队列的大小等于 min(backlog，somaxconn)，backlog 是我们创建套接字时通过代码指定的，java 中默认是 50，somaxconn 也是一个内核参数，默认是 128，不同的操作系统版本会有一定的区别。  
&emsp;&emsp;另外内核参数 tcp_syn_retries 控制的是 SYN 包重试的次数(第一次握手中的 SYN 包)，tcp_synack_retries 控制的是 SYN+ACK 包重试的次数(第二次握手中的 SYN+ACK 包)。tcp_syncookies 参数设置为 1 可以防止 SYN-Flood 攻击，但开启后会占用服务端资源，需要酌情考虑。  
&emsp;&emsp;最后，在第三次握手中，全连接队列满了以后，会根据内核参数 tcp_abort_on_overflow 来决定怎么处理连接，当设置为 0 时表示的是直接丢弃客户端发送来的 ACK 包，当设置为 1 时表示的是服务端会发送 RST 通知给客户端，客户端会出现 connection reset peer 异常。  
&emsp;&emsp;关于内核参数的查看，可以使用 sysctl -a 查看所有内核参数，也可以在**/proc/sys/net/ipv4** 目录下查看网络相关的内核参数。通过 ss 或者 netstat 命令可以查看网络相关的统计值，这两个工具可以帮助我们快速定位线上问题。  
&emsp;&emsp;另外可以通过修改**/etc/sysctl.conf** 文件来修改相关内核参数，然后使用 sysctl -p 使修改的参数生效。  
```
                        Listening
     SYN_SEND  --SYN->                      sync queue   半连接队列
            <--SYN+ACK-- SYN_RCVD
     ESTABLISHED --ACK-->                   accept queue 全连接队列
                        ESTABLISHED

```
#### backlog 这个参数就是用来限制全连接队列的大小的
### 参考[backlog与TCP三次握手之间不得不说的事](https://juejin.im/post/5e204abe6fb9a02ff67d3f6d)
