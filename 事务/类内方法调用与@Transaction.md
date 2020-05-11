[Spring事务管理嵌套事务详解 : 同一个类中，一个方法调用另外一个有事务的方法,为什么不生效](https://blog.csdn.net/levae1024/article/details/82998386)  
```
@Service
class A{
    @Transactinal
    method b(){
    ... c();
    }
    
    method a(){    //标记1
        b();
    }
    method c(){    //标记 
        ...
    }
}
 
//Spring扫描注解后，创建了另外一个代理类，并为有注解的方法插入一个startTransaction()方法：
class proxy$A{
    A objectA = new A();
    method b(){    //标记2
        startTransaction();
        objectA.b();
    }
 
    method a(){    //标记3
        objectA.a();    //由于a()没有注解，所以不会启动transaction，而是直接调用A的实例的a()方法
    }
}
```
#### &emsp;&emsp;方法b被事务注解修饰，a调用b，事务无相关性，b调用c，bc事务一致  
