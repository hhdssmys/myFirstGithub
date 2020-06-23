#### 参考 ：
1. [spring-kafka深入探秘](https://zhuanlan.zhihu.com/p/93445381)不错  
2. [SpringBoot Kafka 整合使用](https://juejin.im/post/5d406a925188255d352ab24e)  
#### 正文       
1. 依赖  
```
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
  <version>1.1.1.RELEASE</version>
</dependency>
```
2. 配置文件 application.properties  
```
#============== kafka ===================# 指定kafka 代理地址，可以多个
spring.kafka.bootstrap-servers=192.168.153.135:9092
##=============== provider  =======================
#spring.kafka.producer.retries=0# 每次批量发送消息的数量
spring.kafka.producer.batch-size=16384spring.kafka.producer.buffer-memory=33554432
## 指定消息key和消息体的编解码方式
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
##=============== consumer  =======================# 指定默认消费者group id
spring.kafka.consumer.group-id=test-consumer-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.enable-auto-commit=true
spring.kafka.consumer.auto-commit-interval=100
## 指定消息key和消息体的编解码方式
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```
3. 发送  
```
@Autowired
private KafkaTemplate<String, String> kafkaTemplate;
*********
kafkaTemplate.send("zhisheng", gson.toJson(message));
```
这个 topic 在 Java 程序中是不需要提前在 Kafka 中设置的，因为它会在发送的时候自动创建你设置的 topic,默认的分区数和副本数为如下Broker参数来设定  
num.partitions = 1 #默认Topic分区数  
num.replica.fetchers = 1 #默认副本数  
4. 消费  
```
@KafkaListener(topics = {"zhisheng"})
public void listen(ConsumerRecord<?, ?> record) {
    Optional<?> kafkaMessage = Optional.ofNullable(record.value());
    if (kafkaMessage.isPresent()) {
        Object message = kafkaMessage.get();
        log.info("----------------- record =" + record);
        log.info("------------------ message =" + message);
    }
}
```
### 创建新的Topic
##### 程序启动时创建Topic 
```
@Bean
public KafkaAdmin admin(KafkaProperties properties){
    KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
    admin.setFatalIfBrokerNotAvailable(true);
    return admin;
}
```
##### 代码逻辑中创建
```
AdminClient client = AdminClient.create(properties.buildAdminProperties());
 if(client !=null){
     try {
         Collection<NewTopic> newTopics = new ArrayList<>(1);
         newTopics.add(new NewTopic("topic-kl",1,(short) 1)); // see here 
         client.createTopics(newTopics);
     }catch (Throwable e){
         e.printStackTrace();
     }finally {
         client.close();
     }
 }
```
#### api方式创建  
```
@Test
public void testCreateTopic()throws Exception{
    ZkClient zkClient =new ZkClient("127.0.0.1:2181", 3000, 3000, ZKStringSerializer$.MODULE$)
    String topicName = "topic-kl";
    int partitions = 1;
    int replication = 1;
    AdminUtils.createTopic(zkClient,topicName,partitions,replication,new Properties());
}
```

