转载文章  
[Spring(AbstractRoutingDataSource)实现动态数据源切换](https://blog.51cto.com/linhongyu/1615895)  
[spring 动态切换数据源 多数据库](https://blog.csdn.net/laojiaqi/article/details/78964862)

## spring动态切换数据源
1. 使用场景  
  1）项目A需实现数据同步到另一项目B数据库中，在不改变B项目的情况下，只好选择项目A中切换数据源，直接把数据写入项目B的数据库中。  
这种需求，在数据同步与定时任务中经常需要  
  2）对于数据量在1千万，单个mysql数据库就可以支持，但是如果数据量大于这个数的时候，例如1亿，那么查询的性能就会很低。此时需要  
对数据库做水平切分，常见的做法是按照用户的账号进行hash，然后选择对应的数据库。这样就数据落入不同的库中。  
2. 解决方案  
  那么问题来了，该如何解决多数据源问题呢？不光是要配置多个数据源，还得能灵活动态的切换数据源。以spring+hibernate框架项目为例：  
<div align=center>![传统数据源关系](https://blog.51cto.com/linhongyu/1615895)</div> 
&ensp;&ensp;&ensp;&ensp;单个数据源绑定给sessionFactory，再在Dao层操作，若多个数据源的话，那不是就成了下图：  
<div align=center>![单个数据源绑定](https://blog.51cto.com/linhongyu/1615895)</div>  
&ensp;&ensp;&ensp;&ensp;可见，sessionFactory都写死在了Dao层，若我再添加个数据源的话，则又得添加一个sessionFactory。所以比较好的做法应该是下图：  
<div align=center>![多个数据源绑定](https://blog.51cto.com/linhongyu/1615895)</div>

&ensp;&ensp;&ensp;&ensp;接下来就为大家讲解下如何用spring来整合这些数据源，同样以spring+hibernate配置为例  
3. 实现原理  
&ensp;&ensp;&ensp;&ensp;1）扩展Spring的AbstractRoutingDataSource抽象类（该类充当了DataSource的路由中介, 能有在运行时, 根据某种key值来动态切换到真正的DataSource上。）  
&ensp;&ensp;&ensp;&ensp;从AbstractRoutingDataSource的源码中：  
```
  public abstract class AbstractRoutingDataSource extends AbstractDataSource implements InitializingBean
``` 
&ensp;&ensp;&ensp;&ensp;我们可以看到，它继承了AbstractDataSource，而AbstractDataSource不就是javax.sql.DataSource的子类，So我们可以分析下它的成员变量与getConnection方法:  
```
    private Map<Object, Object> targetDataSources;
    private Object defaultTargetDataSource;
    private Map<Object, DataSource> resolvedDataSources;
```
* targetDataSources中保存了key和数据库连接的映射关系
* defaultTargetDataSource表示默认的链接
* resolvedDataSources这个数据结构是通过targetDataSources构建而来，存储的结构也是数据库标识和数据源的映射关系
这三个成员变量在配置多数据源映射bean声明时是对应可参考的来源  
以下是最重要的getConnection()方法：  
```
  public Connection getConnection() throws SQLException {  
      return determineTargetDataSource().getConnection();  
  }
  public Connection getConnection(String username, String password) throws SQLException {  
      return determineTargetDataSource().getConnection(username, password);  
  }
```
&ensp;&ensp;&ensp;&ensp;获取连接的方法中，重点是determineTargetDataSource()方法，看源码：  
```
    /** 
     * Retrieve the current target DataSource. Determines the 
     * {@link #determineCurrentLookupKey() current lookup key}, performs 
     * a lookup in the {@link #setTargetDataSources targetDataSources} map, 
     * falls back to the specified 
     * {@link #setDefaultTargetDataSource default target DataSource} if necessary. 
     * @see #determineCurrentLookupKey() 
     */  
    protected DataSource determineTargetDataSource() {  
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");  
        Object lookupKey = determineCurrentLookupKey();  
        DataSource dataSource = this.resolvedDataSources.get(lookupKey);  
        if (dataSource == null && (this.lenientFallback || lookupKey == null)) {  
            dataSource = this.resolvedDefaultDataSource;  
        }  
        if (dataSource == null) {  
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");  
        }  
        return dataSource;  
    }
```  
 &ensp;&ensp;&ensp;&ensp;上面这段源码的重点在于determineCurrentLookupKey()方法，这是AbstractRoutingDataSource类中的一个抽象方法，而它的返回值是你所要用的数据源dataSource的key值，有了这个key值，resolvedDataSource（这是个map,由配置文件中设置好后存入的）就从中取出对应的DataSource，如果找不到，就用配置默认的数据源。  
 &ensp;&ensp;&ensp;&ensp;看完源码，应该有点启发了吧，没错！你要扩展AbstractRoutingDataSource类，并重写其中的determineCurrentLookupKey()方法，来实现数据源的切换：  
 
 ```
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 重写determineCurrentLookupKey，其返回想要切换至生效的数据源的标识key
 *
 * @create 2019-07-25 9:50
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        //初始化并没有设置DataSourceType，为空会去取默认值
        return DataSourceKeyContextHolder.getDataSourceType()==null?null:DataSourceKeyContextHolder.getDataSourceType().getSourceKey();
    }

}
```  
DataSourceKeyContextHolder这个类则是我们自己封装的对数据源进行操作的类：  
```
/**
 * 切换数据源的标识key
 *
 * @create 2019-07-25 9:52
 */
public class DataSourceKeyContextHolder {
    private static ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

    public static void setDataSourceType(DataSourceType datasource) {
        contextHolder.set(datasource);
    }

    public static DataSourceType getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
```
DataSourceType这个是个枚类，用来罗列我们的数据源标识：  
```
/**
 * 数据源类型枚举
 *
 * @create 2019-07-25 9:52
 */
public enum DataSourceType {
    /**
     * 数据源 A
     */
    DATA_SOURCE_PKUFI("dataSource_A", "数据源A"),
    /**
     * 数据源A
     */
    DATA_SOURCE_KPI("dataSource_B", "数据源B");
    /**
     * 数据源的标识key
     */
    private final String sourceKey;
    /**
     * 数据源的来源描述
     */
    private final String sourceDesc;

    private DataSourceType(final String sourceKey, final String sourceDesc) {
        this.sourceKey = sourceKey;
        this.sourceDesc = sourceDesc;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    /**
     * 返回具体数据源的key
     * @param dataSourceType
     * @return 数据源的key
     */
    public static String  getDataSourceKey(DataSourceType dataSourceType){
        return dataSourceType.getSourceKey();
    }
}
```  
&ensp;&ensp;&ensp;&ensp;2）有人就要问，那你setDataSource这方法是要在什么时候执行呢？当然是在你需要切换数据源的时候执行啦。手动在代码中调用写死吗？这是多蠢的方法，当然要让它动态咯。所以我们可以应用spring aop来设置，把配置的数据源类型都设置成为注解标签，在service层中需要切换数据源的方法上，写上注解标签，调用相应方法切换数据源咯（就跟你设置事务一样）：   
```
@DataSourceSwitch(dataSource = DataSourceType.DATA_SOURCE_KPI)
public String getDataBaseBranchCodeLevel2ByComCode(String comCode) { ... }
```  
&ensp;&ensp;&ensp;&ensp;当然，注解标签的用法可能很少人用到，但它可是个好东西哦，大大的帮助了我们开发  
```
import com.gcol.qy.core.extend.datasourcev2.DataSourceType;
import java.lang.annotation.*;
/**
 * 该注解要修改数据源，故应该在dao层往上，确定数据源再在Dao生产sessionFactory，推荐在service上使用
 * 
 * 此注解修改后已经可自动切换数据源并在方法结束后还原数据源
 * 使用限制：同类方法间调用，该注解注释于被调用方法是不起作用的
 * @create 2019-07-25 10:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface DataSourceSwitch {
    DataSourceType dataSource() default DataSourceType.DATA_SOURCE_PKUFI;
}
```  
当然注解只是一个标志，具体的行为还是在aop增强的增强方法里：  
```
import com.gcol.qy.core.extend.datasourcev2.DataSourceKeyContextHolder;
import com.gcol.qy.core.extend.datasourcev2.DataSourceType;
import com.gcol.qy.web.system.utils.DataSourceSwitch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

/**
 * 目前只支持单数据源事务，最好在事务开启前确定数据源
 * 单数据源事务是ok的，但如果多数据源需要同时使用一个事务呢？
 * 
 * 注解@DataSourceSwitch的切面类，将注解的设置实际应用起来
 * @create 2019-07-25 10:25
 */

@Aspect
@Component
public class DataSourceSwitchInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceSwitchInterceptor.class);

    @Pointcut(value = "@annotation(com.*.mypackage.DataSourceSwitch)")
    public void dataSourceSwitchPointcut(){}

    @Around("dataSourceSwitchPointcut()")
    public Object  dataSourceSwitchAround(ProceedingJoinPoint joinPoint){
        Object result = null;
        DataSourceType oldDataSource = DataSourceKeyContextHolder.getDataSourceType();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        LOGGER.info("方法{}执行前原本数据源是{}",method.getName(),oldDataSource==null?null:oldDataSource.getSourceDesc());
        DataSourceSwitch dataSourceSwitch = method.getAnnotation(DataSourceSwitch.class);
        DataSourceType newDataSource = dataSourceSwitch.dataSource();
        LOGGER.info("方法{}注解的新数据源是{}",method.getName(),newDataSource.getSourceDesc());
        //注册新注解
        DataSourceKeyContextHolder.setDataSourceType(newDataSource);
        try {
            //原方法调用
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage(),throwable);
        }finally{
            //切换回原数据源
            DataSourceKeyContextHolder.setDataSourceType(oldDataSource);
            LOGGER.error("方法{}结束后的当前数据源是{}",method.getName(), DataSourceKeyContextHolder.getDataSourceType()==null?null: DataSourceKeyContextHolder.getDataSourceType().getSourceDesc());
        }
        return result;
    }

}
```  
4. 配置文件  
&ensp;&ensp;&ensp;&ensp;为了精简篇幅，省略了无关本内容主题的配置。项目中单独分离出application-database.xml，关于数据源配置的文件。  
```
<?xml version="1.0" encoding="UTF-8"?>
<!-- Spring 数据库相关配置 放在这里 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">
        
    <bean id = "dataSource1" class = "com.alibaba.druid.pool.DruidDataSource"
      init-method="init" destroy-method="close">   
        <property name="url" value="${db1.url}"/>
        <property name = "user" value = "${db1.user}"/>
        <property name = "password" value = "${db1.pwd}"/>
        
        <!-- 初始化连接大小 -->
        <property name="initialSize" value="0"/>
        <!-- 连接池最大使用连接数量 -->
        <property name="maxActive" value="20"/>
        <!-- 连接池最大空闲 -->
        <property name="maxIdle" value="20"/>
        <!-- 连接池最小空闲 -->
        <property name="minIdle" value="5"/>
        <!-- 获取连接最大等待时间 -->
        <property name="maxWait" value="60000"/>
        <property name="validationQuery" value="${validationQuery.sqlserver}"/>
        <property name="testOnBorrow" value="false"/>
        <property name="testOnReturn" value="false"/>
        <property name="testWhileIdle" value="true"/>

        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="60000"/>
        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="25200000"/>

        <!-- 打开removeAbandoned功能 -->
        <property name="removeAbandoned" value="true"/>
        <!-- 1800秒，也就是30分钟 -->
        <property name="removeAbandonedTimeout" value="1800"/>
        <!-- 关闭abanded连接时输出错误日志 -->
        <property name="logAbandoned" value="true"/>

        <!-- 开启Druid的监控统计功能 -->
        <property name="filters" value="stat"/>
        <!--<property name="filters" value="mergeStat" /> -->
        <!-- Oracle连接是获取字段注释 -->
        <property name="connectProperties">
            <props>
                <prop key="remarksReporting">true</prop>
            </props>
        </property>
    </bean>

    <bean id = "dataSource2" class = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource">
        <property name="url" value="${db2.url}"/>
        <property name = "user" value = "${db2.user}"/>
        <property name = "password" value = "${db2.pwd}"/>
        <property name="autoReconnect" value="true"/>
        <property name="useUnicode"  value="true"/>
        <property name="characterEncoding" value="UTF-8"/>
    </bean>
    
    <!-- 配置多数据源映射关系，这里的配置对应抽象类 AbstractRoutingDataSource 成员变量-->
    <bean id="dataSource" class="com.datasource.test.util.database.DynamicDataSource">
        <property name="targetDataSources">
            <map key-type="java.lang.String">
                <entry key="dataSource1" value-ref="dataSource1"></entry>
                <entry key="dataSource2" value-ref="dataSource2"></entry>
            </map>
        </property>
        <!-- 默认目标数据源为你主库数据源 -->
        <property name="defaultTargetDataSource" ref="dataSource1"/>
    </bean>
    
    <bean id="sessionFactory"
          class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--<property name="entityInterceptor" ref="hiberAspect" />-->
        <property name="hibernateProperties">
            <props>
                <!--<prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop> -->
                <prop key="hibernate.dialect">${hibernate.dialect}</prop>
                <prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.format_sql">true</prop>
                <prop key="hibernate.temp.use_jdbc_metadata_defaults">false</prop>
            </props>
        </property>
        <!-- 注解方式配置 -->
        <property name="packagesToScan">
            <list>
                <value>com.*.mypackage.pojo.*</value>
            </list>
        </property>
    </bean>
    
    <!-- 配置事物管理器，在*ServiceImpl里写@Transactional就可以启用事物管理 -->
    <!--
    测试时会报错：getCurrentSession()时报错，no session found for current thread
    如果配置了TranactionManager事务，无论是@Transactional注解或者声明的方式配置的事务边界，
    那么Spring都会在开始事务之前通过AOP的方式为当前线程创建Session，此时调用getCurrentSession()是没有问题的
    -->
    <bean name="transactionManager"
          class="org.springframework.orm.hibernate4.HibernateTransactionManager">
        <property name="sessionFactory" ref="sessionFactory"></property>
    </bean>
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```  
5. 尚未解决的问题  
&ensp;&ensp;&ensp;&ensp;多数据源切换是成功了，但牵涉到事务呢？单数据源事务是ok的，但如果多数据源需要同时使用一个事务呢？这个问题有点头大，网络上有人提出用atomikos开源项目实现JTA分布式事务处理。你怎么看？






 
























