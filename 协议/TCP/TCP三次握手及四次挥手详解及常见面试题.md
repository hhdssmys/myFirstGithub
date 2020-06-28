#### [TCP三次握手及四次挥手详解及常见面试题](https://blog.csdn.net/libaineu2004/article/details/85374818)  
为什么需要2MSL时间？   
等待2MSL时间主要目的是：防止最后一个ACK包对方没有收到，那么对方在超时后将重发第三次握手的FIN包，主动关闭端接到重发的FIN包后可以再发一个ACK应答包。
