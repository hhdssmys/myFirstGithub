#### 转载于[生产环境中，autoCreateTopicEnable为什么不能设置为true](https://www.javazhiyin.com/57294.html)
消息发送者在到默认路由信息时，其队列数量，会选择DefaultMQProducer#defaultTopicQueueNums与Nameserver返回的的队列数取最小值
