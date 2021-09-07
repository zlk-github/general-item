## 1 JDK 普通练习项目

### 1.1 dynamicproxy包 动态代理

    JDK原生动态代理：是由java内部的反射机制来实现的，需要目标类实现统一的接口。
    
    CGLIB动态代理：借助asm来实现;可以通过将asm生成的类进行缓存，解决asm生成类过程低效问题。

### 1.2 aggregate包 java集合

### 1.3 reflect包 反射与泛型

### 1.4 thread包 多线程与线程池

    1.普通线程创建与线程常见知识。
    2.线程池
    3.常见锁java
      volatile
      单机锁LOCK，synchronized
      分布式锁：redis,zookpeer,mysql等实现
      乐观锁CAS等
    4.线程的原子性问题，可见性问题，有序性问题

### 1.5 jvm包 虚拟机调优

