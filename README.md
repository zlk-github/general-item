<h1>后端技术体系</h1>

前言：内容有参考于https://github.com/xingshaocheng/architect-awesome.git，致谢。

<b style="color:red">推荐:</b> [《Java技术书籍大全》 - awesome-java-books](https://github.com/sorenduan/awesome-java-books)


**详细见对应项目下README.md文件**

**标记(非重点)为了解层面，以下为大部分为目录，后期逐步完善。**。

* [JAVA技术体系](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/README.md#JAVA技术体系)
  * [JDK版本(重点JAVA8)](https://github.com/zlk-github/general-item/blob/master/README.md#JDK版本(重点JAVA8))
  * [JAVA集合](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA集合)
  * [并发线程](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/README.md#并发线程)
    * [Java 并发](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#java-并发)
     * [多线程与线程池](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#多线程与线程池)
     * [线程安全](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#线程安全)
     * [AutomicInteger、AtomicReference等](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#AutomicInteger、AtomicReference等)
     * [一致性、事务](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#一致性事务)
         * [事务 ACID 特性](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#事务-acid-特性)
         * [事务的隔离级别](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#事务的隔离级别)
         * [MVCC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#mvcc)
     * [锁](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/lock/README-LOCK.md#锁)
         * [Java中的锁和同步类](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#java中的锁和同步类)
         * [公平锁 &amp; 非公平锁](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#公平锁--非公平锁)
         * [悲观锁](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#悲观锁)
         * [乐观锁 &amp; CAS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#乐观锁--cas)
         * [ABA 问题](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#aba-问题)
         * [CopyOnWrite容器](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#copyonwrite容器)
         * [RingBuffer](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ringbuffer)
         * [可重入锁 &amp; 不可重入锁](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/lock/reentrant/README-REENTRANT.md#可重入锁--不可重入锁)
         * [互斥锁 &amp; 共享锁](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#互斥锁--共享锁)
         * [死锁](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#死锁)
  * [JAVA虚拟机及优化](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA虚拟机及优化)
  * [JAVA设计模式](https://github.com/zlk-github/general-item/blob/master/README.md#JAVAJAVA设计模式)
     * [设计模式的六大原则](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#设计模式的六大原则)
     * [23种常见设计模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#23种常见设计模式)
     * [应用场景](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#应用场景)
     * [单例模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#单例模式)
     * [责任链模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#责任链模式)
     * [MVC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#mvc)
     * [DDD](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#DDD)
     * [IOC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ioc)
     * [AOP](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#aop)
     * [UML](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#uml)
     * [微服务思想](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#微服务思想)
         * [康威定律](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#康威定律)
  * [JAVA框架与源码](https://github.com/zlk-github/general-item/blob/master/README.md#JAVAJAVA设计模式)
    * [Spring与Spring MVC框架与源码](https://github.com/zlk-github/general-item/blob/master/README.md#Spring与SpringMVC框架与源码)
    * [Spring Boot框架与源码](https://github.com/zlk-github/general-item/blob/master/README.md#SpringBoot框架与源码)
    * [分布式框架与源码（Spring Cloud Alibaba为主）](https://github.com/zlk-github/general-item/blob/master/README.md#分布式框架与源码（SpringCloudAlibaba))
    * [Netty框架与源码](https://github.com/zlk-github/general-item/blob/master/README.md#Netty框架与源码)
    * [数据库持久框架与源码（MyBatis与MyBatis-plus）](https://github.com/zlk-github/general-item/blob/master/README.md#数据库持久框架与源码（MyBatis与MyBatis-plus）)                                
    * [日志框架Log4j、Log4j2(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#日志框架Log4j、Log4j2)
  * [JAVA零散基础](https://github.com/zlk-github/general-item/blob/master/README.md#JAVA零散基础)
  
* [数据结构与算法](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/README.md#数据结构与算法)
  * [算法基础知识](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/README-BASIC.md#算法基础知识)
      * [空间复杂度与时间复杂度](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/README-BASIC.md#空间复杂度与时间复杂度)
  * [数据结构](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/structure/README.md#数据结构)
    * [数据结构之线性结构与链式结构](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据结构之线性结构与链式结构)
    * [队列与栈](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#队列与栈)
     * [集合](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#集合)
     * [链表、数组](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#链表数组)
     * [字典、关联数组](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#字典关联数组)
     * [树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#树)
         * [二叉树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#二叉树)
         * [完全二叉树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#完全二叉树)
         * [平衡二叉树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#平衡二叉树)
         * [二叉查找树（BST）](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#二叉查找树bst)
         * [红黑树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#红黑树)
         * [B，B+，B*树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#b-bb树)
         * [LSM 树](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#lsm-树)
     * [BitSet](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#bitset)
      * [堆与图](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#堆与图)
  * [常用算法](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/common/README.md#常用算法)
    * [排序、查找算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#排序查找算法)
        * [选择排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#选择排序)
        * [冒泡排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#冒泡排序)
        * [插入排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#插入排序)
        * [快速排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#快速排序)
        * [归并排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#归并排序)
        * [希尔排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#希尔排序)
        * [堆排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#堆排序)
        * [计数排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#计数排序)
        * [桶排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#桶排序)
        * [基数排序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#基数排序)
        * [二分查找](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#二分查找)
        * [Java 中的排序工具](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#java-中的排序工具)
    * [布隆过滤器](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#布隆过滤器)
    * [字符串比较](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#字符串比较)
        * [KMP 算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#kmp-算法)
    * [深度优先、广度优先](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#深度优先广度优先)
    * [贪心算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#贪心算法)
    * [回溯算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#回溯算法)
    * [剪枝算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#剪枝算法)
    * [动态规划](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#动态规划)
    * [朴素贝叶斯](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#朴素贝叶斯)
    * [推荐算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#推荐算法)
    * [最小生成树算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#最小生成树算法)
    * [最短路径算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#最短路径算法)

* [数据库](https://github.com/zlk-github/general-item/tree/master/src/main/java/com/zlk/com/zlk/db/README.md#数据库)
    * [关系型数据库](https://github.com/zlk-github/general-item/tree/master/src/main/java/com/zlk/com/zlk/db/relational/mysql#关系型数据库)
      * [MySQL](https://github.com/zlk-github/general-item/tree/master/src/main/java/com/zlk/com/zlk/db/relational/mysql#mysql)
          * [基础理论](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#基础理论)
              * [关系数据库设计的三大范式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#关系数据库设计的三大范式)
              * [关系数据库设计的三大范式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#关系数据库设计的三大范式)
              * [关系数据库特性与事务隔离级别](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#关系数据库特性与事务隔离级别)
              * [数据库锁与MVCC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据库锁与MVCC)
          * [原理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#原理)
          * [MySQL引擎Innodb和Myisam](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#MySQL引擎Innodb和Myisam)
          * [数据库优化](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据库优化)
          * [索引与索引底层结构](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#索引与索引底层结构)
              * [索引类型](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#索引类型)
              * [索引创建注意与索引失效](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#索引创建注意与索引失效)
              * [索引之b+树与HASH](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#索引之b+树与HASH)
          * [执行计划explain与Sql优化](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#执行计划explain与Sql优化)
          * [MySQl分库分表](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#MySQl分库分表)
          * [MySQl集群](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#MySQl集群)
    * [NoSQL](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#nosql)
        * [Redis(key-value非关系内存缓存型)](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README.md#Redis(key-value非关系内存缓存型))
        * [文档型非数据库MongoDB](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#文档型非数据库MongoDB)
        * [Hbase](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#hbase)
        * [Hive](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#Hive)
        * [列数据库ClickHouse](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#列数据库ClickHouse)

* [搜索引擎](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#搜索引擎)
    * [搜索引擎原理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#搜索引擎原理)
    * [Lucene](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#lucene)
    * [Elasticsearch](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#elasticsearch)
    * [Solr(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#solr)
    * [sphinx](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#sphinx)

* [性能](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#性能)
    * [性能优化方法论](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#性能优化方法论)
    * [容量评估](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#容量评估)
    * [CDN 网络](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#cdn-网络)
    * [连接池](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#连接池)
    * [性能调优](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#性能调优)

* [分布式设计](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式设计)
    * [扩展性设计](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#扩展性设计)
    * [稳定性 &amp; 高可用](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#稳定性--高可用)
        * [硬件负载均衡](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#硬件负载均衡)
        * [软件负载均衡](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#软件负载均衡)
        * [限流](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#限流)
        * [应用层容灾](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#应用层容灾)
        * [跨机房容灾](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#跨机房容灾)
        * [容灾演练流程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#容灾演练流程)
        * [平滑启动](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#平滑启动)
    * [数据库扩展](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据库扩展)
        * [读写分离模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#读写分离模式)
        * [分片模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分片模式)
    * [服务治理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#服务治理)
        * [服务注册与发现](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#服务注册与发现)
        * [服务路由控制](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#服务路由控制)
    * [分布式一致](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式一致)
        * [CAP 与 BASE 理论](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#cap-与-base-理论)
        * [分布式锁Redisson](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-REDISSON.md#分布式锁Redisson)
        * [分布式一致性算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式一致性算法)
            * [PAXOS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#paxos)
            * [Zab](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#zab)
            * [Raft](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#raft)
            * [Gossip](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#gossip)
            * [两阶段提交、多阶段提交](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#两阶段提交多阶段提交)
        * [幂等](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#幂等)
        * [分布式一致方案](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式一致方案)
        * [分布式 Leader 节点选举](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式-leader-节点选举)
        * [分布式事务atomikos](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式-leader-节点选举)
        * [分布式事务XA,TC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式-leader-节点选举)
        * [TCC(Try/Confirm/Cancel) 柔性事务](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#tcctryconfirmcancel-柔性事务)
    * [分布式文件系统](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式文件系统)
      * [阿里OSS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#阿里OSS)
      * [七牛云](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#七牛云)
    * [唯一ID 生成](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#唯一id-生成)
        * [全局唯一ID](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#全局唯一id)
    * [一致性Hash算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#一致性hash算法)
    * [日志系统ELK](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#日志系统ELK)
    * [异常监控 Sentry](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#异常监控Sentry)
    * [分布式链路跟踪 SkyWalking](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式链路跟踪SkyWalking)
                    
* [中间件](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#中间件)
    * [Web Server](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#web-server)
        * [Nginx](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#nginx)            			
        * [OpenResty](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#openresty)  
        * [Tengine](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#Tengine)  
        * [Apache Httpd](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#apache-httpd)
        * [Tomcat(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#tomcat)
            * [架构原理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#架构原理)
            * [调优方案](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#调优方案)
        * [Jetty](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#jetty)
    * [缓存(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#缓存)
        * [本地缓存](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#本地缓存)
    * [客户端缓存(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#客户端缓存)
    * [服务端缓存](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#服务端缓存)
        * [Web缓存(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#web缓存)
        * [Memcached(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#memcached)
        * [Redis](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README.md#redis)
            * [架构](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#架构)
            * [回收策略](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#回收策略)
        * [Tair](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#tair)
    * [消息队列](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#消息队列)
        * [消息总线](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#消息总线)
        * [消息的顺序](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#消息的顺序)
        * [RabbitMQ](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#rabbitmq)
        * [RocketMQ](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#rocketmq)
        * [ActiveMQ(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#activemq)
        * [Kafka](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#kafka)
        * [Redis 消息推送(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#redis-消息推送)
        * [ZeroMQ](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#zeromq)
    * [定时调度](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#定时调度)
        * [单机定时调度(非重点)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#单机定时调度)
        * [分布式定时调度](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#分布式定时调度)
            * [xxl-job](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#xxl-job)
    	
    * [RPC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#rpc)
        * [Dubbo](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#dubbo)
        * [Thrift](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#thrift)
        * [gRPC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#grpc)
    * [数据库中间件](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据库中间件)
        * [Sharding Jdbc](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#sharding-jdbc)
        * [PXC](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#PXC)
        * [MyCat](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#MyCat)
        	
    * [日志系统](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#日志系统)
        * [日志搜集](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#日志搜集)
    * [配置中心](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#配置中心)
    * [API 网关](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#api-网关)
	
* [计算机原理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#计算机原理)
    * [CPU](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#cpu)
        * [多级缓存](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#多级缓存)
    * [进程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#进程)
    * [线程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#线程)
    * [协程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#协程)
    * [Linux](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#linux)

* [计算机网络](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#计算机网络)
    * [协议](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#协议)
        * [OSI 七层协议](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#osi-七层协议)
        * [TCP/IP](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#tcpip)
        * [HTTP](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#http)
        * [HTTP2.0](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#http20)
        * [HTTPS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#https)
    * [网络模型](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#网络模型)
        * [Epoll](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#epoll)
        * [Java NIO](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#java-nio)
        * [kqueue](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#kqueue)
    * [连接和短连接](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#连接和短连接)
    * [框架](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#框架)
    * [零拷贝（Zero-copy）](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#零拷贝zero-copy)
    * [序列化(二进制协议)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#序列化二进制协议)
        * [Hessian](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#hessian)
        * [Protobuf](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#protobuf)
    * [域名相关](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#域名相关)

* [搜索引擎](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#搜索引擎)
    * [搜索引擎原理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#搜索引擎原理)
    * [Lucene](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#lucene)
    * [Elasticsearch](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#elasticsearch)
    * [Solr](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#solr)
    * [sphinx](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#sphinx)
	
* [性能](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#性能)
    * [性能优化方法论](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#性能优化方法论)
    * [容量评估](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#容量评估)
    * [CDN 网络](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#cdn-网络)
    * [连接池](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#连接池)
	
* [大数据](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#大数据)
    * [流式计算](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#流式计算)
        * [Storm](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#storm)
        * [Flink](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#flink)
        * [Kafka Stream](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#kafka-stream)
        * [应用场景](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#应用场景-1)
    * [Hadoop](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#hadoop)
        * [HDFS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#hdfs)
        * [MapReduce](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#mapreduce)
        * [Yarn](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#yarn)
    * [Spark](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#spark)
    * [hive与HBase](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#hive与HBase)
    * [ETL](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ETL)
    
* [安全](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#安全)
    * [web 安全](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#web-安全)
        * [XSS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#xss)
        * [CSRF](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#csrf)
        * [SQL 注入](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#sql-注入)
        * [Hash Dos](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#hash-dos)
        * [脚本注入](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#脚本注入)
        * [漏洞扫描工具](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#漏洞扫描工具)
        * [验证码](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#验证码)
    * [DDoS 防范](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ddos-防范)
    * [用户隐私信息保护](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#用户隐私信息保护)
    * [序列化漏洞](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#序列化漏洞)
    * [加密解密](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#加密解密)
        * [对称加密](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#对称加密)
        * [哈希算法](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#哈希算法)
        * [非对称加密](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#非对称加密)
    * [服务器安全](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#服务器安全)
    * [数据安全](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据安全)
        * [数据备份](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#数据备份)
    * [网络隔离](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#网络隔离)
        * [内外网分离](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#内外网分离)
        * [登录跳板机](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#登录跳板机)
    * [权限认证](https://github.com/zlk-github/general-item/blob/master/README.md#权限认证)  
       * [权限认证Auth2与JWT](https://github.com/zlk-github/general-item/blob/master/README.md#权限认证Auth2与JWT)  
       * [单点登录(SSO)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#单点登录sso)

* [运维 &amp; 统计 &amp; 技术支持](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#运维--统计--技术支持)
    * [常规监控](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#常规监控)
    * [APM](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#apm)
    * [统计分析](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#统计分析)
    * [持续集成(CI/CD)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#持续集成cicd)
        * [Jenkins](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#jenkins)
        * [环境分离](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#环境分离)
    * [自动化运维](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#自动化运维)
        * [Ansible](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ansible)
        * [puppet](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#puppet)
        * [chef](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#chef)
    * [测试](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#测试)
        * [TDD 理论](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#tdd-理论)
        * [单元测试](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#单元测试)
        * [压力测试](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#压力测试)
          * [jmeter]( https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/pressure/test/README-JMETER.md#jmeter)
        * [全链路压测](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#全链路压测)
        * [A/B 、灰度、蓝绿测试](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ab-灰度蓝绿测试)
    * [虚拟化](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#虚拟化)
        * [KVM](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#kvm)
        * [Xen](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#xen)
        * [OpenVZ](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#openvz)
    * [容器技术](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#容器技术)
        * [Docker](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#docker)
        * [K8s](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#K8s)
    * [云技术](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#云技术)
        * [OpenStack](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#openstack)
    * [DevOps](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#devops)
    * [文档管理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#文档管理)

* [python](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#python)

* [设计等重点关注](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#设计重点关注)
    * [技术选型](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#技术选型)
    * [架构设计图](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#架构设计图)
    * [数据库设计图](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#数据库设计图)
    * [系统文档设计与UML等](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#系统文档设计与UML等)
    * [PPT与文档设计,演讲](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#PPT与文档设计,演讲)
    * [另：风险评估与沟通](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#风险评估与沟通)
    * [团体管理能力](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#团体管理能力)
                   
* [面试题目录](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#面试题目录)

* [Arthas（阿尔萨斯)](https://arthas.aliyun.com/doc/#Arthas（阿尔萨斯）)

* [stackoverflow](https://stackoverflow.com/（stackoverflow）)
  
* [扩展](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/algorithm/data/README-EXAM.md#扩展)
    * [设计思想 &amp; 开发模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#设计思想--开发模式)
        * [DDD(Domain-driven Design - 领域驱动设计)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#ddddomain-driven-design---领域驱动设计)
            * [命令查询职责分离(CQRS)](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#命令查询职责分离cqrs)
            * [贫血，充血模型](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#贫血充血模型)
        * [Actor 模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#actor-模式)
        * [响应式编程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#响应式编程)
            * [Reactor](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#reactor)
            * [RxJava](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#rxjava)
            * [Vert.x](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#vertx)
        * [DODAF2.0](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#dodaf20)
        * [Serverless](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#serverless)
        * [Service Mesh](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#service-mesh)
    * [项目管理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#项目管理)
        * [架构评审](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#架构评审)
        * [重构](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#重构)
        * [代码规范](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#代码规范)
        * [代码 Review](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#代码-review)
        * [RUP](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#rup)
        * [看板管理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#看板管理)
        * [SCRUM](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#scrum)
        * [敏捷开发](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#敏捷开发)
        * [极限编程（XP）](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#极限编程xp)
        * [结对编程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#结对编程)
        * [PDCA 循环质量管理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#pdca-循环质量管理)
        * [FMEA管理模式](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#fmea管理模式)
    * [通用业务术语](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#通用业务术语)
    * [技术趋势](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#技术趋势)
    * [政策、法规](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#政策法规)
        * [法律](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#法律)
            * [严格遵守刑法253法条](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#严格遵守刑法253法条)
    * [架构师素质](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#架构师素质)
    * [团队管理](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#团队管理)
        * [招聘](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#招聘)
    * [资讯](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#资讯)
        * [行业资讯](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#行业资讯)
        * [公众号列表](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#公众号列表)
        * [博客](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#博客)
            * [团队博客](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#团队博客)
            * [个人博客](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#个人博客)
        * [综合门户、社区](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#综合门户社区)
        * [问答、讨论类社区](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#问答讨论类社区)
        * [行业数据分析](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#行业数据分析)
        * [专项网站](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#专项网站)
        * [其他类](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#其他类)
        * [推荐参考书](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#推荐参考书)
            * [在线电子书](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#在线电子书)
            * [纸质书](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#纸质书)
                * [开发方面](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#开发方面)
                * [架构方面](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#架构方面)
                * [技术管理方面](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#技术管理方面)
                * [基础理论](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#基础理论-1)
                * [工具方面](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#工具方面)
                * [大数据方面](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#大数据方面)
    * [技术资源](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#技术资源)
        * [开源资源](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#开源资源)
        * [手册、文档、教程](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#手册文档教程)
        * [在线课堂](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#在线课堂)
        * [会议、活动](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#会议活动)
        * [常用APP](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#常用app)
        * [找工作](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#找工作)
        * [工具](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#工具)
        * [代码托管](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#代码托管)
        * [文件服务](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#文件服务)
        * [综合云服务商](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#综合云服务商)
            * [VPS](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#vps)

##  后端技术体系

### 1 JAVA技术体系

    主要未JAVA相关技术介绍与使用

#### 1.1 JDK版本(重点JAVA8)

    介绍JDK的版本发展差异，JAVA 8新特性，JAVA 11。


#### 1.1.1 JDK版本介绍

详见：[JDK版本](https://github.com/zlk-github/general-item/tree/master/src/main/java/com/zlk/jdk/README-JDK.md#JDK版本(JDK版本))

    各JDK版本介绍。

#### 1.1.2 JDK8新特性

详见：[重点JAVA 8 && JAVA 11](https://github.com/zlk-github/general-item/tree/master/src/main/java/com/zlk/jdk/jdk8/README.md#JDK版本(重点JAVA8&&JAVA11))

    JAVA 8新特性，JAVA 11。


#### 1.2 JAVA集合

#### 1.3 并发线程

#### 1.3.1 Java 并发

#### 1.3.2 多线程与线程池

#### 1.3.3 线程安全

#### 1.3.4 AutomicInteger等

#### 1.3.5 一致性、事务

	#### 1.3.5.1 事务 ACID 特性
	#### 1.3.5.2 事务的隔离级别
	#### 1.3.5.3 MVCC

#### 1.3.6 锁

	Java中的锁和同步类
	公平锁 &amp; 非公平锁
	悲观锁
	乐观锁 &amp; CAS
	ABA 问题
	CopyOnWrite容器
	RingBuffer
	可重入锁 &amp; 不可重入锁
	互斥锁 &amp; 共享锁
	死锁

#### 1.4 JAVA虚拟机及优化

#### 1.5 JAVA设计模式

#### 1.6 JAVA框架与源码

#### 1.7 JAVA零散基础



===============================================

===============================================



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