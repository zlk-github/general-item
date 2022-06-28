##  common-redis-test

Spring Boot 如何集成redis做缓存(默认过期时间),分布式锁，布隆表达式，消息传递/发布订阅, Redis 事务，redis持久化，redis集群方案等。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git     --(https://github.com/zlk-github/common)

### 1 Redis 介绍

详见：* [Redis介绍与常见问题汇总](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-INTRODUCE.md#Redis介绍与常见问题汇总)

    1.1 Redis为什么快
    1.2 redis版本差异,单多线程等。
    1.3 同类产品比较（优劣）
    1.4 Redis的常用5种数据类型与选择
    1.5 Redis 常见问题
        1.5.1 什么场景适合使用缓存
        1.5.2 Redis为什么是单线程的
        1.5.3 简述Redis的数据淘汰机制
        1.5.4 Redis怎样防止异常数据不丢失
        1.5.5 Redis实现原理
        1.5.6 Redis高可用与数据同步

安装见：[Linux安装Redis教程](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-INIT.md#Linux安装Redis教程)


### 2 Spring Boot 集成redis做缓存

详见： [Spring Boot集成redis做缓存](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-CACHE.md#SpringBoot集成redis做缓存)

    Spring Boot 集成redis缓存工具类。与项目中的缓存使用。

### 3 Spring Boot 集成Redisson做分布式锁

详见：[Spring Boot集成Redisson做分布式锁](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-Redisson.md#SpringBoot集成Redisson做分布式锁)

### 4 Redis穿透，击穿，雪崩
    
    Redis缓存穿透：数据库与缓存中都不存在，黑客大量访问打到数据库；（布隆过滤器/返回空对象）
    Redis缓存击穿：数据库中存在对应值，redi缓存过期，大量请求访问打到数据库；（分布式锁Redisson）
    Redis缓存雪崩：缓存大面积失效，或者重启消耗大量资源。（缓存时间设置随机，启动加到队列,冷热数据分离，预加载等，热数据均匀分布到不同缓存数据库）

代码见： [Redis穿透，击穿，雪崩](https://github.com/zlk-github/common-test/blob/master/common-redis-test/src/main/java/com/zlk/redis/service/CachePenetrationService.java#Redis穿透，击穿，雪崩)

### 5 消息传递/发布订阅（非重点）

参考见：

    https://www.cnblogs.com/yitudake/p/6747995.html
    https://blog.csdn.net/qq_29443327/article/details/107140420?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-1.no_search_link&spm=1001.2101.3001.4242

简介：

    Redis提供了发布订阅功能，可以用于消息的传输，Redis的发布订阅机制包括三个部分，发布者，订阅者和Channel。

    发布者和订阅者都是Redis客户端，Channel则为Redis服务器端，发布者将消息发送到某个的频道，订阅了这个频道的订阅者就能接收到这条消息。Redis的这种发布订阅机制与基于主题的发布订阅类似，Channel相当于主题。

    基于list（链表队列）实现。
    （1）发送消息
        Redis采用PUBLISH命令发送消息，其返回值为接收到该消息的订阅者的数量。
    （2）订阅某个频道
         Redis采用SUBSCRIBE命令订阅某个频道，其返回值包括客户端订阅的频道，目前已订阅的频道数量，以及接收到的消息，其中subscribe表示已经成功订阅了某个频道。
    （3）模式匹配 
        模式匹配功能允许客户端订阅符合某个模式的频道，Redis采用PSUBSCRIBE订阅符合某个模式所有频道，用“”表示模式，“”可以被任意值代替。
    （4）取消订阅 
        Redis采用UNSUBSCRIBE和PUNSUBSCRIBE命令取消订阅，其返回值与订阅类似。

    注：不能提供消息队列的消息持久化。

场景：

    可以用来维护关注列表做推送与关注数查询（如当前你被多少粉丝关注，推送文章给粉丝）


### 6 Redis 事务（非重点）

参考见：https://zhuanlan.zhihu.com/p/135241403

关系型数据中的事务都是原子性的（同一个事务内提交，要么全部成功，要么全部失败叫做原则性），而redis 的事务是非原子性的。
严格的说Redis的命令是原子性的，而事务是非原子性的。

Redis事务相关命令：

    MULTI ：开启事务，redis会将后续的命令逐个放入队列中，然后使用EXEC命令来原子化执行这个命令系列。
    EXEC：执行事务中的所有操作命令。
    DISCARD：取消事务，放弃执行事务块中的所有命令。
    WATCH：监视一个或多个key,如果事务在执行前，这个key(或多个key)被其他命令修改，则事务被中断，不会执行事务中的任何命令。
    UNWATCH：取消WATCH对所有key的监视。

注：
    
    多数事务失败是由语法错误或者数据结构类型错误导致的，语法错误说明在命令入队前就进行检测的（会全部失败），
    而类型错误是在执行时检测的（会部分成功），Redis为提升性能而采用这种简单的事务，这是不同于关系型数据库的，特别要注意区分。

### 7 Redis持久化

详见： [Redis数据持久化](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-PERSISTENCE.md#Redis数据持久化)

由于Redis数据存放在内存中，定期写入磁盘（半持久化）。如果没有配置持久化，redis重启后数据会产生丢失。Redis提供了两种持久化机制，将数据保存到磁盘上，重启后从磁盘恢复数据。
分布为RDB(Redis DataBase)和AOF(Append Only File)。

**RDB**：快照方式，全量备份。将Redis在内存中的数据库记录定时dump到磁盘上的RDB持久化。

**AOF**：日志记录方式。操作日志记录以追加方式写入文件。

|  技术选型  | RDB | AOF |
|  ----  | ----  |----  | 
| 启动优先级| 低  | 高 | 
|  体积 | 小| 大| 
|  恢复速度 | 快| 慢| 
|  数据安全性 | 丢数据 | 根据策略决定 | 
|  轻重 | 重 | 轻| 

### 8 Redis集群方案(高可用)

单机模式会不能实现高可用，从而产生以下几种解决方案。

    1.哨兵模式
    
    2.主从模式
    
    3.集群模式

#### 8.1 哨兵模式

一主二从三哨兵，1个master主节点，2个slave从节点,对所有3个Redis配置sentinel哨兵模式。
当master节点宕机时，通过哨兵(sentinel)重新推选出新的master节点，保证集群的可用性。

哨兵的主要功能：

    1.集群监控：负责监控 Redis master 和 slave 进程是否正常工作。
    2.消息通知：如果某个 Redis 实例有故障，那么哨兵负责发送消息作为报警通知给管理员。
    3.故障转移：如果 master node 挂掉了，会自动转移到 slave node 上。
    4.配置中心：如果故障转移发生了，通知 client 客户端新的 master 地址。
    PS：根据推举机制，集群中哨兵数量最好为奇数(3、5....)

一主二从三哨兵优缺点：

    优点：能满足高可用，且在满足高可用的情况下使用服务器资源最少（相较于主从与集群模式）
    缺点：但是选举时会间断。扩展难，有性能瓶颈。

#### 8.2 主从模式

#### 8.3 集群模式

最少3个master节点，且每个主节点下挂一个slave节点。

    优点：能满足高可用，扩展性好。
    缺点：需要使用大量的服务器资源。

#### 9 Redis 性能指标与优化方案

详见： [Redis 性能指标与优化方案](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-OPTIMIZE.md#Redis性能指标与优化方案)

#### 10 Redis 常用命令

详见： [Redis常用命令](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-COMMAND.md#Redis常用命令)


### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis
    
    Springboot redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    Redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556

    Redisson官网 https://redisson.org/

    Redisson官方文档 https://github.com/redisson/redisson/wiki

    Redisson中文文档（完整） https://github.com/redisson/redisson/wiki/目录

    Redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693
    
    Redis数据结构： https://zhuanlan.zhihu.com/p/344918922
    
    Redis跳跃表：https://www.jianshu.com/p/c2841d65df4c

    Redis跳跃表（详细）： https://www.jianshu.com/p/c2841d65df4c

    Redisson锁 https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8

    Redisson信号量与闭锁 https://blog.csdn.net/qq_43080270/article/details/113184266?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_utm_term~default-0.no_search_link&spm=1001.2101.3001.4242

    Redisson 使用手册 https://www.bookstack.cn/read/redisson-wiki-zh/Redisson项目介绍.md
