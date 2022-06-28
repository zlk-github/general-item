##  Spring Boot集成Redisson做分布式锁(common-redis-test)

Spring Boot集成Redisson做分布式锁。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git    --(https://github.com/zlk-github/common/tree/master/common-redis)

什么是分布式锁：

    当部署多台服务时，由于不是同一个JVM,此时java的本地锁（synchronized或者ReentrantLock等）将不能解决多台服务并发的互斥问题。
    这时候会引入一个三方组件来做统一协调，达到互斥目的，这个就是所谓的分布式锁。
    
    注：开发中不要滥用分布式锁，否则会导致性能大大降低。（相对而言可以使用乐观锁尽量不要使用悲观锁，可以使用分段锁尽量不要使用锁住整个资源）

分布式锁实现与比较

|  技术选型  | 复杂度 <br>（实现成本） | 性能 | 可靠性 | 优点  | 缺点 | 实现技术 |
|  ----  | ----  |----  | ----  |----  |----  | ----  |
| **Redisson分布式锁**  | 中 |高 | 中  | redis性能很高,<br>适合高并发场景 | 客户端挂掉，需要超时释放|基于Redis的lua脚本与看门狗,即Redisson|
| **MySQL分布式锁**  | 低 |低 | 低 | 学习成本低（基于唯一键） | 可用性与性能较差（连接开销大），不加备可重入，锁失效与阻塞锁。（实现这些效果将很复杂）|基于关系型数据库排他锁（乐观锁与悲观锁） |
| **Zookeeper分布式锁**  | 高 |中 | 高 | 具备高可用、可重入、阻塞锁特性，可解决失效死锁问题。 <br>客户端挂掉，节点失去，自动释放| 因为需要频繁的创建和删除节点，性能上不如Redis方式|基于Zookeeper，判断最小节点获取锁（Curator提供的InterProcessMutex是分布式锁的实现） |
| **Memcached分布式锁**  | 中 |高 | 中   | 类似与redis的setnx实现锁| 不能判断key是否存在，需要手动设置过期（不好控制）|基于Memcached缓存，基于add命令加锁 |
    
    一般情况下Redisson分布式锁是不二之选（大部分项目都会使用到Redis，跨语言API。且实现难度一般，性能高，可靠性也不错）
    
    如果项目中有使用到Zookeeper且团队对Zookeeper熟悉，可以考虑选择Zookeeper分布式锁。

### 1 Redisson分布式锁介绍

    Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。
    默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
    如果程序死亡，到过期时间会自动释放。程序未死亡会自动延时到程序执行完后在程序中自动释放（unlock）。

    Redisson分布式锁相比于传统使用Redis的setnx实现分布式锁，多了看门狗，能自动延长锁的寿命。
    （使用setnx需要手动控制过期时间，不好估计方法执行需要多久。锁过期太短会导致方法未执行完锁就被释放，锁过期太长会导致服务出现宕机长时间锁住导致别人也不可以用）

### 2 Spring Boot集成Redisson做分布式锁

#### 2.1 Redisson分布式锁

锁类型介绍见：[锁介绍](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/lock/README-LOCK.md#锁介绍)

#### 2.1.1 可重入锁（Reentrant Lock）

    基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。

    可重入锁介绍：
        避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)

#### 2.1.2 公平锁（Fair Lock）

    基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。
    它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。

    公平锁介绍：
        多线程按照申请锁的顺序获取锁。--（先进先出原则为公平锁）

#### 2.1.3 联锁（MultiLock）

    基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。

    联锁介绍：（全部上锁成功算成功）
        保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）

#### 2.1.4 红锁（RedLock）
    
    基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    
    红锁介绍：（上锁成功大于等于locks.size() / 2 + 1为成功）
        同时加锁，大部分锁节点加锁成功就算成功。locks.size() / 2 + 1为成功

#### 2.1.5 读写锁（ReadWriteLock）

    基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    
    读写锁介绍: 
        分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
        读锁使用共享模式；写锁使用独占模式。（读读共享，读写互斥，写写互斥，）

#### 2.1.6 信号量（Semaphore 计数信号量）

    基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    
    信号量介绍:
        当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
        --如停车位，进出场地（车位有限）。唯一计数。

#### 2.1.7 可过期性信号量（PermitExpirableSemaphore）

    基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    
    可过期性信号量介绍: 
        ID来辨识的信号量设置有效时间。

#### 2.1.8 闭锁（CountDownLatch）
    
    基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    
    闭锁介绍:
        计数清零时执行（如班上20个同学全部离开才能锁教室门）


### 2.2 Spring Boot 2.1.3集成Redisson分布式

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git    --(https://github.com/zlk-github/common/tree/master/common-redis)
    3、需要集成swagger做测试见：https://github.com/zlk-github/common/blob/master/common-web/README-SWAGGER2.md

### 2.2.1 pom.xml(部分)

完整pom.xml参考项目

```jsx
    <!--redis分布式锁redisson-->
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson</artifactId>
        <version>3.5.0</version>
    </dependency>
```

### 2.2.2 application.yaml

```jsx
server:
    port: 8025

################redis配置################
spring:
    redis:
        #数据库索引
        database: 0
        host: localhost
        port: 6379
        password:
        jedis:
            pool:
                #最大连接数
                max-active: 8
                #最大阻塞等待时间(负数表示没限制)
                max-wait: -1
                #最大空闲
                max-idle: 8
                #最小空闲
                min-idle: 0
            #连接超时时间
            timeout: 10000

#是否激活 swagger true or false
swagger:
    enable: true
```

####  2.2.3 Redisson分布式锁-配置类

```java
/**
 * @author likuan.zhou
 * @title: RedisConfig
 * @projectName common
 * @description: Redisson分布式锁-配置类
 * @date 2021/9/16/016 19:05
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password:}")
    private String password;

    @Bean
    public RedissonClient getRedisson(){
        Config config = new Config();
        //单机模式  依次设置redis地址和密码.其余配置未加
        config.useSingleServer().
                setAddress("redis://"+host+":"+port);
        if (StringUtils.isEmpty(password)) {
            config.useSingleServer().setPassword(null);
        } else {
            config.useSingleServer().setPassword(password);
        }

        //哨兵模式(一主两从三哨兵)
        /*config.useSentinelServers()
                .setMasterName("mymaster")
                //可以用"rediss://"来启用SSL连接
                .addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379")
                .addSentinelAddress("127.0.0.1:26319");*/

        // 主库提供写能力，然后主库复制到从库，从库提供读能力。
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});

        // 集群模式配置（多主搭配多从） setScanInterval()扫描间隔时间，单位是毫秒, //可以用"rediss://"来启用SSL连接
        // config.useClusterServers().setScanInterval(2000).addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001").addNodeAddress("redis://127.0.0.1:7002");
        return Redisson.create(config);
    }

}
```

####  2.2.4 Redission分布式锁接口

```java
/**
 * @author likuan.zhou
 * @title: IRedissionLock
 * @projectName common
 * @description: Redission分布式锁接口
 * @date 2021/9/23/023 9:23
 */
public interface IRedissonLock {
    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    /**
     * 加redisson分布式锁（可重入锁--普通类型）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addLock(String key);

    /**
     * 释放redisson分布式锁（可重入锁）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeLock(String key);

    /**
     * 加redisson分布式锁（可重入锁--过期类型（看门狗失效））
     * @param key redis锁key
     * @param time 锁自动释放时间（单位S）
     * @return 执行结果 true成功，false失败
     */
     Boolean addLock(String key,long time);

    /**
     * 加redisson分布式锁（可重入锁--过期（看门狗失效）--等待类型）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addTryLock(String key,long waitTime,long time);

    /**
     * 加redisson分布式锁（可重入锁--异步执行--过期（看门狗失效）--等待类型）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Future<Boolean> addTryLockAsync(String key, long waitTime, long time);

    //=============================== 公平锁（Fair Lock）=====================================
    // 基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
    // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
    /**
     * 加redisson分布式锁(重入锁--公平锁--普通类型)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addFairLock(String key);

    /**
     * 删除redisson分布式锁(重入锁--公平锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeFairLock(String key);

    /**
     * 加redisson分布式锁（可重入锁--公平锁--自定义过期（看门狗失效）--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    Boolean addFairTryLock(String key,long waitTime,long time);

    /**
     * 加redisson分布式锁（可重入锁--公平锁--自定义过期（看门狗失效）--等待--异步类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     RFuture<Boolean> addFairTryLockAsync(String key, long waitTime, long time);

    //=============================== 联锁（MultiLock）=====================================
    // 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）
    /**
     * 加redisson分布式锁(联锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean addMultiLock(RLock... rLocks);

    /**
     * 解除redisson分布式锁(联锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean removeMultiLock(RLock... rLocks);

    //=============================== 红锁（RedLock）=====================================
    // 基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 红锁: 同时加锁，大部分锁节点加锁成功就算成功。locks.size() / 2 + 1为成功
    /**
     * 加redisson分布式锁(红锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean addRedLock(RLock... rLocks);

    /**
     * 解除redisson分布式锁(红锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean removeRedLock(RLock... rLocks);

    //===============================读写锁（ReadWriteLock）=====================================
    // 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    // 读写锁: 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
    // 读锁使用共享模式；写锁使用独占模式。
    /**
     * 加redisson分布式锁(可重入读锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addRwrLock(String key);

    /**
     * 加redisson分布式锁(可重入写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addRwwLock(String key);

    /**
     * 解除redisson分布式锁(可重入读锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeRwrLock(String key);

    /**
     * 解除redisson分布式锁(可重入写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    Boolean removeRwwLock(String key);

    //===============================信号量（Semaphore 计数信号量）=====================================
    // 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 信号量: 当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
    // --如停车位，进出场地（车位有限）。唯一计数。
    /**
     * 信号量初始值设置
     * @param key redis锁key
     * @param initVal 信号量初始值
     * @return 执行结果 true成功，false失败
     */
    Boolean trySetPermits(String key,int initVal);

    /**
     * 信号量使用
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
     Boolean useSemaphore(String key,int val);

    /**
     * 信号量使用
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean useSemaphore(String key);

    /**
     * 释放信号量
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
     Boolean releaseSemaphore(String key,int val);

    /**
     * 释放信号量
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean releaseSemaphore(String key);

    //===============================可过期性信号量（PermitExpirableSemaphore）=====================================
    // 基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 可过期性信号量: ID来辨识的信号量设置有效时间

    /**
     * 可过期性信号量初始值设置
     * @param key redis锁key
     * @param initVal 信号量初始值
     * @return 执行结果 true成功，false失败
     */
     Boolean peTrySetPermits(String key,int initVal);

    /**
     * 可过期性信号量
     * @param key 可过期性信号量标识
     * @param time 信号量过期时间 （单位s）
     * @return 执行结果 true成功，false失败
     */
     Boolean peSemaphore(String key,long time);

    /**
     * 可过期性信号量释放
     * @param key 可过期性信号量标识
     * @return 执行结果 true成功，false失败
     */
     Boolean releasePeSemaphore(String key);

    //=============================== 闭锁（CountDownLatch）=====================================
    // 基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    // 闭锁: 计数清零时执行（如班上20个同学全部离开才能锁教室门）

    /**
     * 闭锁 （初始值）
     * @param key key
     * @param val 闭锁的计数初始值（如教室总人数）
     * @return 执行结果 true成功，false失败
     */
     Boolean trySetCount(String key,long val);

    /**
     * 闭锁 （关门上锁）
     * 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
     * @param key key
     * @return 执行结果 true成功，false失败
     */
     Boolean addCountDownLatch(String key);

    /**
     * 闭锁 （锁计数减一）
     * @param key key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeCountDownLatch(String key);
}

```
####  2.2.5 Redission分布式锁接口实现类

```java
package com.zlk.common.redis.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author likuan.zhou
 * @title: RedissonLockImpl
 * @projectName common
 * @description: Redission分布式锁接口实现
 * @date 2021/9/27/027 8:57
 */
@Slf4j
@Component
public class RedissonLockImpl implements IRedissonLock {
    @Autowired
    private RedissonClient redisson;

    //===============================可重入锁（Reentrant Lock）=====================================
    // 基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)
    // 设置超时类型，看门狗会失效

    /**
     * 加redisson分布式锁（可重入锁--普通类型）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addLock(String key) {
        try {
            RLock lock = redisson.getLock(key);
            lock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 释放redisson分布式锁（可重入锁）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeLock(String key) {
        try {
            RLock lock = redisson.getLock(key);
            lock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁解锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁--过期类型（看门狗失效））
     * @param key redis锁key
     * @param time 锁自动释放时间（单位S）
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addLock(String key,long time) {
        try {
            RLock lock = redisson.getLock(key);
            // 加锁以后time秒钟自动解锁
            // 无需调用unlock方法手动解锁；也可以手动失效
            lock.lock(time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }


    /**
     * 加redisson分布式锁（可重入锁--过期（看门狗失效）--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addTryLock(String key,long waitTime,long time) {
        try {
            RLock lock = redisson.getLock(key);
            // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
            // 说明：尝试加锁的线程等待waitTime秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time秒后锁失效。
            // 无需调用unlock方法手动解锁,锁到期会自动失效；也可以手动失效
            return lock.tryLock(waitTime, time, TimeUnit.SECONDS);
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁--异步执行--过期（看门狗失效）--等待类型）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Future<Boolean> addTryLockAsync(String key,long waitTime,long time) {
        try {
            RLock lock = redisson.getLock(key);
            //lock.lockAsync();
            //lock.lockAsync(10, TimeUnit.SECONDS);
            // 可重入锁-异步执行
            Future<Boolean> future = lock.tryLockAsync(waitTime, time, TimeUnit.SECONDS);
            return future;
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return null;
    }


    //=============================== 公平锁（Fair Lock）=====================================
    //基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    //它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
    //公平锁: 多线程按照申请锁的顺序获取锁。--（先进先出原则为公平锁）

    /**
     * 加redisson分布式锁(重入锁--公平锁--普通类型)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addFairLock(String key) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            fairLock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 删除redisson分布式锁(重入锁-公平锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeFairLock(String key) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            fairLock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁--公平锁--自定义过期（看门狗失效）--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addFairTryLock(String key,long waitTime,long time) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            //上锁以后time秒自动解锁（可重入锁--公平锁--自定义过期）
            //fairLock.tryLock(time,TimeUnit.SECONDS);
            // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁
            fairLock.tryLock(waitTime,time,TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁--公平锁--过期（看门狗失效）--等待--异步类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public RFuture<Boolean>  addFairTryLockAsync(String key,long waitTime,long time) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            //普通异步
            //fairLock.lockAsync();
            //异步，上锁以后time秒自动解锁
            //fairLock.lockAsync(time, TimeUnit.SECONDS);
            // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁
            RFuture<Boolean> future = fairLock.tryLockAsync(waitTime, time, TimeUnit.SECONDS);
            return future;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return null;
    }

    //=============================== 联锁（MultiLock）=====================================
    // 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）

    /**
     * 加redisson分布式锁(联锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addMultiLock(RLock... rLocks) {
        if (rLocks == null) {
            return false;
        }
        try {
            RedissonMultiLock lock = new RedissonMultiLock(rLocks);
            // 同时加锁：lock1 lock2 lock3.......
            // 联锁 所有的锁都上锁成功才算成功。
            lock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--联锁上锁失败。rLocks:{}",rLocks,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(联锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeMultiLock(RLock... rLocks) {
        if (rLocks == null) {
            return false;
        }
        try {
            RedissonMultiLock lock = new RedissonMultiLock(rLocks);
            lock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--联锁解锁失败。rLocks:{}",rLocks,ex);
        }
        return false;
    }

    // 等待实现不赘述
    //RedissonRedLock lock = new RedissonMultiLock(lock1, lock2, lock3);
    // 给lock1，lock2，lock3加锁，如果没有手动解开的话，10秒钟后将会自动解开
    //lock.lock(10, TimeUnit.SECONDS);

    // 为加锁等待100秒时间，并在加锁成功10秒钟后自动解开
    //boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);


    //=============================== 红锁（RedLock）=====================================
    // 基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 红锁: 同时加锁，大部分锁节点加锁成功就算成功。locks.size() / 2 + 1为成功

    /**
     * 加redisson分布式锁(红锁--普通类型)
     * @param rLocks rLocks
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addRedLock(RLock... rLocks) {
        if (rLocks == null) {
            return false;
        }
        try {
            RedissonRedLock lock = new RedissonRedLock(rLocks);
            // 同时加锁：lock1 lock2 lock3.......
            // 红锁在大部分节点上加锁成功就算成功。locks.size() / 2 + 1为成功
            lock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--红锁上锁失败。rLocks:{}",rLocks,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(红锁--普通类型)
     * @param rLocks rLocks
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeRedLock(RLock... rLocks) {
        if (rLocks == null) {
            return false;
        }
        try {
            RedissonRedLock lock = new RedissonRedLock(rLocks);
            lock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--红锁解锁失败。rLocks:{}",rLocks,ex);
        }
        return false;
    }

    // 等待实现不赘述
    //RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
    // 给lock1，lock2，lock3加锁，如果没有手动解开的话，10秒钟后将会自动解开
    //lock.lock(10, TimeUnit.SECONDS);

    // 为加锁等待100秒时间，并在加锁成功10秒钟后自动解开
    //boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);


    //===============================读写锁（ReadWriteLock）=====================================
    // 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    // 读写锁: 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
    // 读锁使用共享模式；写锁使用独占模式。
    /**
     * 加redisson分布式锁(可重入读写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addRwrLock(String key) {
        RReadWriteLock readWriteLock;
        try {
            readWriteLock = redisson.getReadWriteLock(key);
            // 读
            readWriteLock.readLock().lock();
            // 或者 写
            // readWriteLock.writeLock().lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--可重入读写锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁(可重入读写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addRwwLock(String key) {
        RReadWriteLock readWriteLock;
        try {
            readWriteLock = redisson.getReadWriteLock(key);
            // 读
            //readWriteLock.readLock().lock();
            // 或者 写
            readWriteLock.writeLock().lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--可重入读写锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(可重入读写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeRwrLock(String key) {
        try {
            RReadWriteLock readWriteLock = redisson.getReadWriteLock(key);
            readWriteLock.readLock().unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--可重入读写锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(可重入读写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeRwwLock(String key) {
        try {
            RReadWriteLock readWriteLock = redisson.getReadWriteLock(key);
            readWriteLock.writeLock().unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--可重入读写锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    // 等待实现不赘述
    //RReadWriteLock rwlock = redisson.getReadWriteLock(key);
    //rwlock.readLock().lock(10, TimeUnit.SECONDS);
    // 或
    //rwlock.writeLock().lock(10, TimeUnit.SECONDS);

    // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
    //boolean res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
    // 或
    //boolean res = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);


    //===============================信号量（Semaphore 计数信号量）=====================================
    // 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 信号量: 当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
    // --如停车位，进出场地（车位有限）。唯一计数。

    /**
     * 信号量初始值设置
     * @param key redis锁key
     * @param initVal 信号量初始值
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean trySetPermits(String key,int initVal) {
        try {
            //信号量
            RSemaphore semaphore = redisson.getSemaphore(key);
            //信号量初始值initVal
            semaphore.trySetPermits(initVal);
            return true;
        }catch (Exception ex) {
            log.error("信号量使用失败。key:{},initVal:{}",key,initVal,ex);
        }
        return false;
    }


    /**
     * 信号量使用
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean useSemaphore(String key,int val) {
        try {
            //信号量
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值-val，如果为0则一直等待，直到信号量>0
            semaphore.acquire(val);
            return true;
        }catch (Exception ex) {
            log.error("信号量使用失败。key:{},val:{}",key,val,ex);
        }
        return false;
    }

    /**
     * 信号量使用
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean useSemaphore(String key) {
        try {
            //信号量
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值-1，如果为0则一直等待，直到信号量>0
            semaphore.acquire();
            return true;
        }catch (Exception ex) {
            log.error("信号量使用失败。key:{}",key,ex);
        }
        return false;
    }


    /**
     * 释放信号量
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean releaseSemaphore(String key,int val) {
        try {
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值+val，也就是释放信号量
            semaphore.release(val);
            return true;
        }catch (Exception ex) {
            log.error("释放信号量失败。key:{},val:{}",key,val,ex);
        }
        return false;
    }

    /**
     * 释放信号量
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean releaseSemaphore(String key) {
        try {
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值+1，也就是释放信号量
            semaphore.release();
            return true;
        }catch (Exception ex) {
            log.error("释放信号量失败。key:{}",key,ex);
        }
        return false;
    }

    // 等待与异步实现不赘述
    /*   RSemaphore semaphore = redisson.getSemaphore(key);
    semaphore.acquire();
    //或
    semaphore.acquireAsync();
    semaphore.acquire(23);
    semaphore.tryAcquire();
    //或
    semaphore.tryAcquireAsync();
    semaphore.tryAcquire(23, TimeUnit.SECONDS);
    //或
    semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
    semaphore.release(10);
    semaphore.release();
    //或
    semaphore.releaseAsync();*/

    //===============================可过期性信号量（PermitExpirableSemaphore）=====================================
    // 基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 可过期性信号量: ID来辨识的信号量设置有效时间

    /**
     * 可过期性信号量初始值设置
     * @param key redis锁key
     * @param initVal 信号量初始值
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean peTrySetPermits(String key,int initVal) {
        try {
            RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore(key);
            // 可过期性信号量初始值
            semaphore.trySetPermits(initVal);
            return true;
        }catch (Exception ex) {
            log.error("可过期性信号量使用失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 可过期性信号量
     * @param key 可过期性信号量标识
     * @param time 信号量过期时间 （单位s）
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean peSemaphore(String key,long time) {
        try {
            RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore(key);
            // 获取一个信号，有效期只有time秒钟。
            semaphore.acquire(time,TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("可过期性信号量使用失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 可过期性信号量释放
     * @param key 可过期性信号量标识
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean releasePeSemaphore(String key) {
        try {
            RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore(key);
            // 通过标识释放过期信号量
            semaphore.release(key);
            return true;
        }catch (Exception ex) {
            log.error("可过期性信号量释放失败。key:{}",key,ex);
        }
        return false;
    }

    //=============================== 闭锁（CountDownLatch）=====================================
    // 基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    // 闭锁: 计数清零时执行（如班上20个同学全部离开才能锁教室门）

    /**
     * 闭锁 （初始值）
     * @param key key
     * @param val 闭锁的计数初始值（如教室总人数）
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean trySetCount(String key,long val) {
        try {
            RCountDownLatch latch = redisson.getCountDownLatch(key);
            //闭锁的计数初始值（如教室总人数）
            latch.trySetCount(val);
            return true;
        }catch (Exception ex) {
            log.error("闭锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 闭锁 （关门上锁）
     * 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
     * @param key key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addCountDownLatch(String key) {
        try {
            RCountDownLatch latch = redisson.getCountDownLatch(key);
            // 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
            latch.await();
            return true;
        }catch (Exception ex) {
            log.error("闭锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 闭锁 （锁计数减一）
     * @param key key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeCountDownLatch(String key) {
        try {
            RCountDownLatch latch = redisson.getCountDownLatch(key);
            // 表示锁计数减一（如教室离开一人）
            latch.countDown();
            return true;
        }catch (Exception ex) {
            log.error("闭锁（锁计数减一）失败。key:{}",key,ex);
        }
        return false;
    }

}

```

####  2.2.6 Redission常量类

```java
/**
 * @author likuan.zhou
 * @title: RedissonConstant
 * @projectName common-test
 * @description: Redisson常量
 * @date 2021/10/9/009 13:38
 */
public interface RedissonConstant {
    /**过期时间*/
     Long EXPIRE_TIME = 60*60L;

    //kye分割使用:会按文件夹方式分割归类。
    /**字符串类型key（key:String）*/
    String LOCK_KEY = "lock:key";
}

```

####  2.2.7 Redission分布式锁测试接口

```java
package com.zlk.redis.controller;

import com.zlk.common.redis.redisson.IRedissonLock;
import com.zlk.redis.constant.RedissonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author likuan.zhou
 * @title: RedissonLockController
 * @projectName common-test
 * @description: Redisson分布式锁测试接口
 * @date 2021/10/9/009 13:40
 */
@Api(tags = "Redisson锁")
@RestController
@RequestMapping("redisson")
public class RedissonLockController {
    // 注：换其他锁测试时，手动清除一下之前的锁。为了测试方便没有取太多redis键key
    // 注：加锁方法不要使用测试用例测试，要不然方法执行结束相当于宕机，无法为锁续命（到期就会自动失效）。
    // swagger : http://localhost:8015/swagger-ui.html
    // 每次测试前清除锁再测试removeLock（）;
    // 设置日期格式
    final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IRedissonLock redissonLock;

    @Autowired
    private RedissonClient redisson;

    //===============================可重入锁（Reentrant Lock）=====================================
    // 基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)
    // 设置超时类型，看门狗会失效.

    @PostMapping("/addLock")
    @ApiOperation("加可重入锁")
    public String addLock() {
        //加可重入锁-默认30S
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY);
        //注：释放锁需要手动调用 lock.unlock();(宕机后到期会自动失效)。
        //redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "加锁成功";
    }

    @PostMapping("/removeLock")
    @ApiOperation("删除可重入锁")
    public String removeLock() {
        //解锁(手动设置失效时间的不能调用该方法)
        Boolean bl = redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "解锁成功";
    }

    @PostMapping("/addLockTime")
    @ApiOperation("加可重入锁--设置失效时间（看门狗失效）")
    public String addLockTime() {
        //加可重入锁-设置失效时间
        //指定失效时间（到期会自动失效），无需调用unlock方法手动解锁
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY,60);
        //注：自动到期失效;或者宕机后到期自动失效,也可以手动失效
        //redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "加锁成功";
    }

    @PostMapping("/addTryLock")
    @ApiOperation("可重入锁--过期（看门狗失效）--等待类型")
    public String addTryLock() {
        // 使用jmeter压线程进行测试（100线程循环10次）
        // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
        // 说明：尝试加锁的线程等待waitTime（100）秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time（10）秒后锁失效。

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        // 锁获取等待时间100s,锁失效时间10S
        if (redissonLock.addTryLock(RedissonConstant.LOCK_KEY, 100, 10)) {
            System.out.println("------------获取锁情况,锁结果：" + true + "------------");
            try {
                // 执行方法休眠5S,方便看执行结果
                Thread.sleep(5000);
                String end = DATE_FORMAT.format(new Date());
                System.out.println("任务执行完成;start:" + start + ",end：" + end);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 手动解锁，不手动解则到期失效
                redissonLock.removeLock(RedissonConstant.LOCK_KEY);
            }
        } else {
            System.out.println("------------获取锁情况,锁结果：" + false + "------------");
            System.out.println("任务排队中，请勿重复操作！");
        }
        //注：自动到期失效;或者宕机后到期自动失效；不需要调用unlock失效。

       /* 结果如下
        ------------获取锁情况,锁结果：true------------
        任务执行完成;start:2021-10-11 15:50:21,end：2021-10-11 15:50:26
        ------------获取锁情况,锁结果：true------------
        任务执行完成;start:2021-10-11 15:50:26,end：2021-10-11 15:50:31
        ------------获取锁情况,锁结果：true------------
        任务执行完成;start:2021-10-11 15:50:31,end：2021-10-11 15:50:36
        。。。。。。。。。
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        任务执行完成;start:2021-10-11 15:50:21,end：2021-10-11 15:52:01
                ------------获取锁情况,锁结果：true------------
                ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！*/
        return "加锁成功";
    }

    @PostMapping("/addTryLockAsync")
    @ApiOperation("可重入锁--过期（看门狗失效）--等待--异步类型")
    public String addTryLockAsync() {
        // 使用jmeter压线程进行测试（100线程循环10次）
        // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。异步执行
        // 说明：尝试加锁的线程等待waitTime（100）秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time（10）秒后锁失效。

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        // 锁获取等待时间100s,锁失效时间10S
        Future<Boolean> future = redissonLock.addTryLockAsync(RedissonConstant.LOCK_KEY, 100, 10);

        System.out.println("------------获取锁情况,锁结果：" + true + "------------");
        try {
            Boolean bl = future.get();
            if (bl) {
                // 执行方法休眠5S,方便看执行结果
                try {
                    Thread.sleep(5000);
                    String end = DATE_FORMAT.format(new Date());
                    System.out.println("任务执行完成;start:" + start + ",end：" + end);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 手动解锁，不手动解则到期失效
                    redissonLock.removeLock(RedissonConstant.LOCK_KEY);
                }
            } else  {
                System.out.println("------------获取锁情况,锁结果：" + false + "------------");
                System.out.println("任务排队中，请勿重复操作！");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //注：自动到期失效;或者宕机后到期自动失效；不需要调用unlock失效。
       /* ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：false------------
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        ------------获取锁情况,锁结果：false------------*/
        return "加锁成功";
    }

    //=============================== 公平锁（Fair Lock）=====================================
    // 基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
    // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
    // 以下将不在赘述自定义过期，等待与异步锁。

    @PostMapping("/addFairLock")
    @ApiOperation("重入锁--公平锁--普通类型")
    public String addFairLock() {
        // 使用jmeter压线程进行测试（100线程循环2次）

        //它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
        // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。

        //加可重入锁-默认30S,到期看门狗会重置时间
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        //公平锁讲究先进先出原则。

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        Boolean bl = redissonLock.addFairLock(RedissonConstant.LOCK_KEY);
        try {
            Thread.sleep(3000);
            String end = DATE_FORMAT.format(new Date());
            System.out.println("任务执行完成;start:" + start + ",end：" + end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //注：释放锁需要手动调用 lock.unlock();(宕机后到期会自动失效)。
            redissonLock.removeFairLock(RedissonConstant.LOCK_KEY);
        }
       /* lock:key为redis的key
        会生成一个key唯一锁(lock:key)，一个线程排队队列(redisson_lock_queue:{lock:key})，一个zset (redisson_lock_timeout:{lock:key})
        线程进入开始时间一样，结束时间差3S(方法执行)。说明进入了排队执行。
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:17
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:20
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:23
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:26
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:29
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:32
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:35
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:38
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:41
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:44
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:47
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:50
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:53
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:56
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:59
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:43:02*/
        return "加锁成功";
    }

    @PostMapping("/removeFairLock")
    @ApiOperation("释放重入锁--公平锁")
    public String removeFairLock() {
        Boolean bl = redissonLock.removeFairLock(RedissonConstant.LOCK_KEY);
        return "释放重入锁--公平锁";
    }

    //=============================== 联锁（MultiLock）=====================================
    // 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）
    @PostMapping("/addMultiLock")
    @ApiOperation("联锁--可重入--普通类型")
    public String addMultiLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        //联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）
        Boolean bl = this.redissonLock.addMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        //释放锁
        //redissonLock.removeMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "加联锁--可重入--普通类型";
    }

    @PostMapping("/removeMultiLock")
    @ApiOperation("释放联锁--可重入--普通类型")
    public String removeMultiLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        Boolean bl = this.redissonLock.removeMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        //释放锁
        //redissonLock.removeMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "释放联锁--可重入--普通类型";
    }

    //=============================== 红锁（RedLock）=====================================
    // 基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 红锁: 同时加锁，大部分锁节点加锁成功（最少locks.size() / 2 + 1为成功）就算成功。
    @PostMapping("/addRedLock")
    @ApiOperation("红锁--可重入--普通类型")
    public String addRedLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        Boolean bl = this.redissonLock.addRedLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        //释放锁
        //redissonLock.removeRedLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "加红锁--可重入--普通类型";
    }

    @PostMapping("/removeRedLock")
    @ApiOperation("红锁--可重入--普通类型")
    public String removeRedLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        Boolean bl = this.redissonLock.removeRedLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "释放红锁--可重入--普通类型";
    }

    //===============================读写锁（ReadWriteLock）=====================================
    // 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    // 读写锁: 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
    // 读锁使用共享模式；写锁使用独占模式。
    // 以下测试读写锁的：读读共享，读写互斥，写写互斥

    @PostMapping("/addRrLock")
    @ApiOperation("读写锁--可重入--普通类型--读读共享")
    public String addRrLock() {
        //测试读读共享
        for (int i = 0; i < 2; i++) {
            new Thread() {
                @Override
                public void run() {
                    getRLock(RedissonConstant.LOCK_KEY,this);
                }
            }.start();
        }
        /*
        --读写锁，读读共享（线程1和线程2共用锁，数据交叉执行）
        key:lock:key,线程名称：Thread-15读操作任务开始时间：2021-10-13 10:08:29
        key:lock:key,线程名称：Thread-16读操作任务开始时间：2021-10-13 10:08:29
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-15,读操作完毕！
        key:lock:key,线程名称：Thread-16,读操作完毕！
        key:lock:key,线程名称：Thread-15读操作任务结束时间：2021-10-13 10:08:29
        ==============================================================
        key:lock:key,线程名称：Thread-16读操作任务结束时间：2021-10-13 10:08:29
        ==============================================================*/
        return "读写锁--可重入--普通类型--读读共享";
    }

    private void getRLock(String key,Thread thread) {
        //读锁
        this.redissonLock.addRwrLock(key);

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"读操作任务开始时间：" + start);
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("key:"+key+",线程名称："+thread.getName()+",正在进行读操作……");
        }
        System.out.println("key:"+key+",线程名称："+thread.getName()+",读操作完毕！");
        String end = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"读操作任务结束时间：" + end);
        System.out.println("==============================================================");
        //释放读锁
        redissonLock.removeRwrLock(key);
    }

    @PostMapping("/addRwLock")
    @ApiOperation("读写锁--可重入--普通类型--读写互斥")
    public String addRwLock() {
        //测试读写互斥
        //读
        new Thread() {
            @Override
            public void run() {
                getRLock(RedissonConstant.LOCK_KEY,this);
            }
        }.start();

        // 写
        new Thread() {
            @Override
            public void run() {
                getWLock(RedissonConstant.LOCK_KEY,this);
            }
        }.start();
        /*
        --读写锁，读写互斥
        key:lock:key,线程名称：Thread-14写操作任务开始时间：2021-10-13 10:07:04
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,写操作完毕！
        key:lock:key,线程名称：Thread-14写操作任务结束时间：2021-10-13 10:07:04
        ==============================================================
        key:lock:key,线程名称：Thread-13读操作任务开始时间：2021-10-13 10:07:04
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,读操作完毕！
        key:lock:key,线程名称：Thread-13读操作任务结束时间：2021-10-13 10:07:04
        ==============================================================*/
        return "读写锁--可重入--普通类型--读读共享";
    }

    private void getWLock(String key,Thread thread) {
        //写锁
        this.redissonLock.addRwwLock(key);

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"写操作任务开始时间：" + start);
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("key:"+key+",线程名称："+thread.getName()+",正在进行写操作……");
        }
        System.out.println("key:"+key+",线程名称："+thread.getName()+",写操作完毕！");
        String end = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"写操作任务结束时间：" + end);
        System.out.println("==============================================================");
        //释放写锁
        redissonLock.removeRwwLock(key);
    }

    @PostMapping("/addWwLock")
    @ApiOperation("读写锁--可重入--普通类型--写写互斥")
    public String addWwLock() {
        //测试写写互斥
        for (int i = 0; i < 5; i++) {
            // 写
            new Thread() {
                @Override
                public void run() {
                    getWLock(RedissonConstant.LOCK_KEY,this);
                }
            }.start();
        }
        /*
        --读写锁，写写互斥
        key:lock:key,线程名称：Thread-14写操作任务开始时间：2021-10-14 08:30:18
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,写操作完毕！
        key:lock:key,线程名称：Thread-14写操作任务结束时间：2021-10-14 08:30:18
        ==============================================================
        key:lock:key,线程名称：Thread-13写操作任务开始时间：2021-10-14 08:30:18
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,写操作完毕！
        key:lock:key,线程名称：Thread-13写操作任务结束时间：2021-10-14 08:30:19
        ==============================================================
         */
        return "读写锁--可重入--普通类型--读读共享";
    }

    //===============================信号量（Semaphore 计数信号量）=====================================
    // 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 信号量: 当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
    // --如停车位，进出场地（车位有限）。唯一计数。

    @PostMapping("/trySetPermits")
    @ApiOperation("初始化信号量值(停车场总车位)")
    public String trySetPermits() {
        //初始化信号量值为6。只能初始化一次（下次需要初始需要删除key,否则trySetPermits不能再重置值）
        this.redissonLock.trySetPermits(RedissonConstant.LOCK_KEY,6);
        return "初始化信号量值";
    }

    @PostMapping("/useSemaphore")
    @ApiOperation("信号量使用（占用车位，空车位减少）")
    public String useSemaphore() {
        //每调用一次，信号量会减一，如果为0则一直等待，直到信号量>0
        this.redissonLock.useSemaphore(RedissonConstant.LOCK_KEY);
        //每调用一次，信号量会减2，如果为0则一直等待，直到信号量>0
        //this.redissonLock.useSemaphore(RedissonConstant.LOCK_KEY,2);
        System.out.println("抢占车位成功");
        /**
         输出结果（信号量初始6，中途不释放信号量，请求10次，只有6辆车可以抢到车位。其余进入等待）
         抢占车位成功
         抢占车位成功
         抢占车位成功
         抢占车位成功
         抢占车位成功
         抢占车位成功
         */
        return "信号量使用";
    }

    @PostMapping("/releaseSemaphore")
    @ApiOperation("释放信号量（空出车位，空车位增加）")
    public String releaseSemaphore() {
        //这里会将信号量里面的值+1，也就是释放信号量
        this.redissonLock.releaseSemaphore(RedissonConstant.LOCK_KEY);
        //这里会将信号量里面的值+2，也就是释放信号量
        //this.redissonLock.releaseSemaphore(RedissonConstant.LOCK_KEY,2);
        System.out.println("释放车位成功");
        /**
         输出结果（信号量初始6，中途不占用信号量，请求10次，释放了10次（超出车位，需要做限制））
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
         */

        // 伪代码
        // 注： 实际使用中需要控制释放总数量不要大于信号量初始值（如停车场车位有限，空车位不能大于总车位）
        // 当前信号量数
        // int permits = redisson.getSemaphore(RedissonConstant.LOCK_KEY).drainPermits();
        // if(（permits+释放值）<信号量初始值) {//释放信号量}
        return "释放信号量";
    }

    //===============================可过期性信号量（PermitExpirableSemaphore）=====================================
    // 基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 可过期性信号量: ID来辨识的信号量设置有效时间
    // 和不过期信号量相比，可过期性信号量有持有时间限制。该处不赘述。


    //=============================== 闭锁（CountDownLatch）=====================================
    // 基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    // 闭锁: 计数清零时执行（如班上20个同学全部离开才能锁教室门）
    @PostMapping("/trySetCount")
    @ApiOperation("初始化闭锁值（教室总人数）")
    public String trySetCount() {
        //初始化闭锁值为20
        this.redissonLock.trySetCount(RedissonConstant.LOCK_KEY,20);
        return "初始化闭锁值（教室总人数）";
    }

    @PostMapping("/removeCountDownLatch")
    @ApiOperation("闭锁值-1（教室离开一人）")
    public String removeCountDownLatch() {
        //闭锁值-1（教室离开一人）
        this.redissonLock.removeCountDownLatch(RedissonConstant.LOCK_KEY);
        System.out.println("有人提着书包跑路了");

        /*
        //先执行trySetCount初始20人，执行addCountDownLatch(锁门方法)无输出。
        //当removeCountDownLatch执行20次后，addCountDownLatch方法输出（人跑完了，关闭教室门回家。）
        //结果如下：
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        人跑完了，关闭教室门回家。*/
        return "教室离开一人";
    }

    @PostMapping("/addCountDownLatch")
    @ApiOperation("闭锁（关门教室门，上锁）")
    public String addCountDownLatch() {
        // 闭锁（关门上锁）
        // 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
        this.redissonLock.addCountDownLatch(RedissonConstant.LOCK_KEY);
        System.out.println("人跑完了，关闭教室门回家。");
        return "关门上锁，拉闸断电！";
    }

}

```

####  2.2.8 Redission分布式服务启动类

注解可以看需要添加

```java
//扫描到配置的redis工具类与配置类及其他需要spring管理的bean
@ComponentScan(basePackages={"com.zlk"})
//需要排除阿里默认数据源，否则启动报错
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
//Swagger2
@EnableSwagger2
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
```

####  2.2.9 测试

部分测试结果见RedissonLockController

* [jmeter使用教程]( https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/pressure/test/README-JMETER.md#jmeter使用教程)

### 参考

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis
    
    Springboot redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    Redisson官网 https://redisson.org/

    Redisson官方文档 https://github.com/redisson/redisson/wiki

    Redisson中文文档（完整） https://github.com/redisson/redisson/wiki/目录

    Redisson锁 https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8

    Redisson信号量与闭锁 https://blog.csdn.net/qq_43080270/article/details/113184266?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_utm_term~default-0.no_search_link&spm=1001.2101.3001.4242

    Redisson 使用手册 https://www.bookstack.cn/read/redisson-wiki-zh/Redisson项目介绍.md