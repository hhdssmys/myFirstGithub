1. 先看异常的分类  
error是一定会回滚的  
这里Exception是异常，他又分为运行时异常RuntimeException和非运行时异常  
    
      
可查的异常（checked exceptions）:Exception下除了RuntimeException外的异常  默认不捕获   
不可查的异常（unchecked exceptions）:RuntimeException及其子类和错误（Error） 默认捕获   
如果不对运行时异常进行处理，那么出现运行时异常之后，要么是线程中止，要么是主程序终止。
如果不想终止，则必须捕获所有的运行时异常，决不让这个处理线程退出。队列里面出现异常数据了，正常的处理应该是把异常数据舍弃，然后记录日志。不应该由于异常数据而影响下面对正常数据的处理。  
非运行时异常是RuntimeException以外的异常，类型上都属于Exception类及其子类。如IOException、SQLException等以及用户自定义的Exception异常。对于这种异常，JAVA编译器强制要求我们必需对出现的这些异常进行catch并处理，否则程序就不能编译通过。所以，面对这种异常不管我们是否愿意，只能自己去写一大堆catch块去处理可能的异常。  
开始主题@Transactional如果只这样写，
Spring框架的事务基础架构代码将默认地只在抛出运行时和unchecked exceptions时才标识事务回滚也就是说，
当抛出个RuntimeException 或其子类例的实例时。（Errors 也一样 - 默认地 - 标识事务回滚。）从事务方法中抛出的Checked exceptions将 ****不 被标识进行事务回滚。  
1. 让checked例外也回滚：在整个方法前加上 @Transactional(rollbackFor=Exception.class)  
2. 让unchecked例外不回滚： @Transactional(notRollbackFor=RunTimeException.class)  
3. 不需要事务管理的(只查询的)方法：@Transactional(propagation=Propagation.NOT_SUPPORTED)  
注意：   
如果异常被try｛｝catch｛｝了，事务就不回滚了，如果想让事务回滚必须再往外抛try｛｝catch｛throw Exception｝。  
注意：  
Spring团队的建议是你在具体的类（或类的方法）上使用 @Transactional 注解，而不要使用在类所要实现的任何接口上。你当然可以在接口上使用 @Transactional 注解，但是这将只能当你设置了基于接口的代理时它才生效。因为注解是不能继承的，这就意味着如果你正在使用基于类的代理时，那么事务的设置将不能被基于类的代理所识别，而且对象也将不会被事务代理所包装（将被确认为严重的）。因此，请接受Spring团队的建议并且在具体的类上使用 @Transactional 注解。  
@Transactional 注解标识的方法，处理过程尽量的简单。尤其是带锁的事务方法，能不放在事务里面的最好不要放在事务里面。可以将常规的数据库查询操作放在事务前面进行，而事务内进行增、删、改、加锁查询等操作。  

转载于：  [需要在Transactional注解指定rollbackFor或者在方法中显示的rollback](https://blog.csdn.net/qq_16605855/article/details/79653282)
