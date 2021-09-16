<h1>后端技术体系</h1>

前言：内容有参考于https://github.com/xingshaocheng/architect-awesome.git，致谢。

<b style="color:red">推荐:</b> [《Java技术书籍大全》 - awesome-java-books](https://github.com/sorenduan/awesome-java-books)


详细见对应项目下README.md文件

* [JAVA](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/README.md#JAVA)
  * [JAVA集合](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA集合)
  * [JAVA多线程](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA多线程)
  * [JAVA虚拟机及优化](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA虚拟机及优化)
  * [JAVA设计模式](https://github.com/zlk-github/general-item/blob/master/README.md#JAVAJAVA设计模式)
  * [JAVA零散基础](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA零散基础)
  * [JAVA框架与源码](https://github.com/zlk-github/general-item/blob/master/README.md#JAVAJAVA设计模式)

* [数据结构与算法](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据结构与算法)
  * [数据结构](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据结构与算法)
  * [常用算法](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据结构与算法)

* [并发编程](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据结构与算法)
    * [数据结构](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据结构与算法)
    * [常用算法](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据结构与算法)

* [数据库](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据库)
  * [关系型数据库](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据库)
  * [非关系型数据库](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据库)
  
* [中间件](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README.md#数据库)

##  后端技术体系

### JAVA

#### JAVA集合

#### JAVA多线程

##### 线程基础

##### 线程池

##### 线程的使用与常见问题

https://mp.weixin.qq.com/s?__biz=MzA3ODIxNjYxNQ==&mid=2247493401&idx=2&sn=08dcf94d57a9ec9e69bb2fbfd4c57a3d&chksm=9f448bd1a83302c7710b3a0d331bf0e70e7ac96e1941933e7027677406c257fb72d7c5fabab5&scene=21#wechat_redirect

#####  JVM调优

##### JVM基础知识

##### JVM调优案例

#####  JAVA框架与源码

IOC与AOP
  
    IOC（Inverse of Control）：依赖注入，控制反转，也可以称为依赖倒置，是Spring的对象管理容器。
    AOP 面向切面编程，是一种编程思想，做解耦与复用。主要的功能是：日志记录，性能统计，安全控制，事务处理，异常处理等等，解决代码复用。

    https://blog.csdn.net/fz13768884254/article/details/83538709
    @Aspect:作用是把当前类标识为一个切面供容器读取
    @Pointcut：Pointcut是植入Advice的触发条件。每个Pointcut的定义包括2部分，一是表达式，二是方法签名。方法签名必须是 public及void型。可以将Pointcut中的方法看作是一个被Advice引用的助记符，因为表达式不直观，因此我们可以通过方法签名的方式为 此表达式命名。因此Pointcut中的方法只需要方法签名，而不需要在方法体内编写实际代码。
    @Around：环绕增强，相当于MethodInterceptor
    @AfterReturning：后置增强，相当于AfterReturningAdvice，方法正常退出时执行
    @Before：标识一个前置增强方法，相当于BeforeAdvice的功能，相似功能的还有
    @AfterThrowing：异常抛出增强，相当于ThrowsAdvice
    @After: final增强，不管是抛出异常或者正常退出都会执行

MVC

###### Spring与SpringMVC

  @Transactional

###### Spring Boot

全局异常处理，日志切面等

###### 分布式框架

####### spring cloud

####### spring cloud alibaba

####### dubbo与zk

###### MyBatis与MyBatis-plus

--------------------------------
--------------------------------

### 数据结构与算法（algorithm.data）

#### 算法的空间复杂度与时间复杂度

#### 常见数据结构

#### 常见排序算法

#### 常见算法题

--------------------------------
--------------------------------

###  设计模式

23中常见设计模式与设计原则。MVC、IOC、AOP、UML。需要画出UML图。

#### 设计原则

#### 设计模式与选择

##### 创建型设计模式

##### 行为型设计模式

##### 结构型设计模式

https://blog.csdn.net/qqxx6661/article/details/84194122

--------------------------------
--------------------------------

###  中间件

#### 缓存中间件

#### redis

#### 消息队列

##### ActiveMQ
##### rabbitmq
##### rocketmq
##### kafka

#### 搜索引擎

##### ELK

##### solar

#### web相关

##### nginx

#### 定时任务

##### XXl-job

#### 文件存储

##### OSS

##### 七牛云

#### NIO框架Netty

--------------------------------
--------------------------------

###  数据库

#### 关系型数据库

#### MySQl

#### ORACLE与PostgreSQL

#### 非关系型数据库

##### redis

##### mongodb

##### clickhouse

--------------------------------
--------------------------------

### 大数据

hbase，hive,hadoop,spark,ELK等。

--------------------------------
--------------------------------

### python

--------------------------------
--------------------------------

###  网络

--------------------------------
--------------------------------

#### 网络七层协议

#### TCP/IP

#### HTTP与HTTPS

#### TCP与UDP

    TCP与UDP区别，TCP的三次握手，四次挥手。

#### 网络模型

    网络模型
        Epoll
        Java NIO
        kqueue
    《web优化必须了解的原理之I/o的五种模型和web的三种工作模式》
    《select、poll、epoll之间的区别总结》
    《BIO与NIO、AIO的区别》
    《两种高效的服务器设计模型：Reactor和Proactor模型》

#### 长连接与短连接

###  CDN加速与DNS域名解析

--------------------------------
--------------------------------

### 操作系统

#### 计算机原理

#### linux

#### linux常用命令

--------------------------------
--------------------------------

###  运维开发&测试&技术支持


#### 运维

    linux,docker,K8s,DevOps
##### 常规监控
##### APM
##### 统计分析
##### 虚拟化
    KVM
    Xen
    OpenVZ
##### 容器技术
    Docker
##### 云技术
    OpenStack
##### DevOps
##### 持续集成(CI/CD)
    Jenkins
    环境分离
##### 自动化运维
    Ansible
    puppet
    chef

####  测试

##### TDD 理论
##### 单元测试
##### 压力测试
##### 全链路压测
##### A/B 、灰度、蓝绿测试
##### 自动化测试

--------------------------------
--------------------------------
###  设计相关（设计方案与设计图）

--------------------------------
--------------------------------

### 参考

    https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#docker?utm_source=wechat_session&utm_medium=social&utm_oi=1110970923947220992