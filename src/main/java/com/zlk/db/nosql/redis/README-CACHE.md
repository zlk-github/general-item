##  Spring Boot 集成Redis做缓存(common-redis-test)

Spring Boot 如何集成redis做缓存。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git    --(https://github.com/zlk-github/common/tree/master/common-redis)


Spring Boot 集成Redis缓存工具类。

### 1 Redis做缓存介绍

#### 1.1 Redis的常用5种数据类型

    **string 字符串**（可以为整形、浮点型和字符串，统称为元素）
        -- String字符串或者json字符串，常规下使用最多，存放字符串。
        -- string 类型的值最大能存储 512MB。
        -- 常用命令：get、set、incr、decr、mget等。
        -- 底层结构：简单动态字符串(SDS)
    
    **list 列表**（实现队列,元素不唯一，先入先出原则）  
        -- 类比java中LinkedList，比如twitter的关注列表，粉丝列表等都可以用Redis的list结构来实现。
           可以用作栈与队列。内存开销稍大，更新删除等容易，离链表两端远查询会稍慢。
        -- 常用命令：lpush（添加左边元素）,rpush,lpop（移除左边第一个元素）,rpop,lrange（获取列表片段，LRANGE key start stop）等。
        -- 底层结构：双向链表实现

    **set 集合**（各不相同的元素） 
        -- 类比java中HashSet，redis中set集合是通过hashtable实现的，没有顺序，适合存放需要去重的数据。
        -- 常用命令：sadd,spop,smembers,sunion 等。
        -- 底层结构：intset或者hashtable
            --如果value可以转成整数值，并且长度不超过512的话就使用intset存储，否则采用hashtable。
            -- intset编码：使用整数集合作为底层实现，set对象包含的所有元素都被保存在intset整数集合里面
            -- hashtable编码：使用字典作为底层实现，字典键key包含一个set元素，而字典的值则都为null

    **hash** hash散列值（hash的key必须是唯一的） 
        -- 类比java中HashMap (list转换后的Map; 一般为key:map<item,Object>,其中item为Object对象主键) ，
           适合存放转换后对象列表（通过id查询对应数据，如用户信息）。
        --常用命令：hget,hset,hgetall 等。
        -- 底层结构：可以是ziplist或者hashtable
            -- 当哈希对象保存的键值对数量小于 512，并且所有键值对的长度都小于 64 字节时，使用ziplist(压缩列表)存储；否则使用 hashtable 存储。
            -- 其中，ziplist底层使用压缩列表实现：
                保存同一键值对的两个节点紧靠相邻，键key在前，值vaule在后
                先保存的键值对在压缩列表的表头方向，后来在表尾方向
            -- hashtable底层使用字典实现，Hash对象种的每个键值对都使用一个字典键值对保存：
                字典的键为字符串对象，保存键key
                字典的值也为字符串对象，保存键值对的值

    **sort set** 有序集合  
        -- 类比java中HashSet，但是有分值比重，适合做去重的排行榜等热数据。
        -- 常用命令：zadd,zrange,zrem,zcard等
        -- 底层结构：分别是：ziplist、skiplist（跳跃表）
        -- 分别是：ziplist、skiplist。当zset的长度小于 128，并且所有元素的长度都小于 64 字节时，使用ziplist存储；否则使用 skiplist 存储（跳跃表是二分查找思想）。
    
    另：范围查询，Bitmaps，Hyperloglogs 和地理空间（Geospatial）索引半径查询

#### 1.2 Redis与其他缓存方案比较和什么场景适合使用缓存

##### 1.2.1 Redis与其他缓存方案比较

Redis和Memcached的其他区别如下：

    Redis和Memcached都是将数据存放在内存中，都是内存数据库。不过Memcached还可用于缓存其他东西，例如图片、视频等等；
    
    Redis不仅仅支持简单的K/V类型的数据，同时还提供List，Set，Hash等数据结构的存储；
    
    虚拟内存–Redis当物理内存用完时，可以将一些很久没用到的Value 交换到磁盘；
    
    过期策略–Memcached在set时就指定，例如:set key1 0 0 8,即永不过期。Redis可以通过例如expire 设定，例如:expire name 10；
    
    分布式–设定Memcached集群，利用magent做一主多从;Redis可以做一主多从。都可以一主一从；
    
    存储数据安全–Memcached挂掉后，数据没了；Redis可以定期保存到磁盘（持久化）；
    
    灾难恢复–Memcached挂掉后，数据不可恢复; Redis数据丢失后可以通过AOF恢复；
    
    Redis支持数据的备份，即Master-Slave模式的数据备份；
    
    应用场景不一样：Redis出来作为NoSQL数据库使用外，还能用做消息队列、数据堆栈和数据缓存等；Memcached适合于缓存SQL语句、数据集、用户临时性数据、延迟查询数据和Session等。

##### 1.2.2 什么场景适合使用缓存

数据特性：数据量小，用户访问量大，数据变动小，数据共享等。

    1、热点数据的缓存 --常用
        访问量大的数据。（如数据字典）
    2、限时业务的运用 --常用
        需要过期时间限制的数据。（短信严重码，优惠活动）
    3、计数器相关问题 --常用
        原则性递增，递减。（秒杀库存，每天接口调用次数限制，手机短信发送次数限制）
    4、排行榜相关问题 --常用
        活动中的排行榜热点数据。
    5、分布式锁  --常用
        利用redis的setnx命令进行，setnx："set if not exists"就是如果不存在则成功设置缓存同时返回1，否则返回0。-- 实际中一般使用Redisson(setnx的分布式锁封装)
    6、延时操作
        实现类似rabbitmq、activemq等消息中间件的延迟队列服务实现。
    7、set实现并集
    8、队列
        由于redis有list push和list pop这样的命令，所以能够很方便的执行队列操作。
    9、可以实现消息发布/订阅

### 2 Spring Boot 集成Redis做缓存

Spring Boot 集成Redis缓存工具类。

#### 2.1 pom.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>org.zlk</groupId>
    <artifactId>common-redis-test</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.5.RELEASE</version>
    </parent>
    
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <common.zlk.version>1.0-SNAPSHOT</common.zlk.version>
        <springfox-swagger2.version>2.9.2</springfox-swagger2.version>
        <commons-lang3.version>3.7</commons-lang3.version>
        <lombok.version>1.18.6</lombok.version>
        <fastjson.version>1.2.58</fastjson.version>
    </properties>
    
    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
    
        <!-- common包集合start -->
        <!--  <dependency>
               <groupId>org.zlk</groupId>
               <artifactId>common-zlk</artifactId>
               <version>${common.zlk.version}</version>
           </dependency>-->
        <dependency>
            <groupId>org.zlk</groupId>
            <artifactId>common-redis</artifactId>
            <version>${common.zlk.version}</version>
        </dependency>
    
        <!--common包集合end -->
    
        <!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>${springfox-swagger2.version}</version>
        </dependency>
    
        <!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>${springfox-swagger2.version}</version>
        </dependency>
    
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>${commons-lang3.version}</version>
        </dependency>
    
        <!--alibaba fastjson-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>${fastjson.version}</version>
        </dependency>
    
    </dependencies>
</project>
```

#### 2.2 配置文件application.yaml

```java
server:
    port: 8015

################redis配置################
spring:
    redis:
        #数据库索引
        database: 0
        host: localhost
        port: 6379
        password:
        jedis:
            #pool:
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
```

#### 2.3 Redis缓存配置类

```java
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author likuan.zhou
 * @title: RedisConfig
 * @projectName common
 * @description: redis 配置类
 * @date 2021/9/16/016 19:05
 */
@Configuration
//开启注解
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * retemplate相关配置
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);


        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // 全局开启AutoType，不建议使用
        // ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // 建议使用这种方式，小范围指定白名单
        //ParserConfig.getGlobalInstance().addAccept("com.xiaolyuh.");

        // 值采用json序列化
        redisTemplate.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式(new StringRedisSerializer()指定hash中map的key为string，本处未使用)
        //redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 对hash类型的数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
```
#### 2.3 Redis缓存工具类

```java

impor lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author likuan.zhou
 * @title: RedisUtil
 * @projectName common
 * @description: redis操作工具类
 * @date 2021/9/16/016 19:25
 */
@Component
@Slf4j
public class RedisUtil {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 删除缓存
     * @param key redis键
     * @return 执行结果 true成功，false失败
     */
    public Boolean del(String key){
        try {
            return redisTemplate.delete(key);
        }catch (Exception ex) {
            log.error("删除redis缓存失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 批量删除缓存
     * @param keys redis键set集合
     * @return 执行结果 true成功，false失败
     */
    public Long del(Set<String> keys){
        return redisTemplate.delete(keys);
    }

    //==========================String==============================
    /**
     * 入普通缓存（字符串类型）
     * @param key redis键
     * @param value 值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean set(String key,Object value,long time){
        try {
            redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 入普通缓存（字符串类型）
     * --key不存在则新增，key已存在则覆盖原来值。
     * @param key redis键
     * @param value 值
     * @return 执行结果 true成功，false失败
     */
    public Boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 通过key获取普通缓存（字符串类型）
     * @param key redis键
     * @return 执行结果 true成功，false失败
     */
    public Object get(String key){
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{}",key,ex);
        }
        return null;
    }


    //==========================hash（MAP）==============================
    /**
     * 入map到缓存（hash表）
     * @param key redis键
     * @param map map值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public <T,V> Boolean hmSet(String key, Map<T,V> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,map,ex);
        }
        return false;
    }

    /**
     * 入map到缓存（hash表）
     * @param key redis键
     * @param map map值
     * @return 执行结果 true成功，false失败
     */
    public <T,V> Boolean hmSet(String key, Map<T,V> map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,map,ex);
        }
        return false;
    }

    /**
     * 入键值对到map缓存（hash表）
     * @param key redis键
     * @param item map中键
     * @param value map中item对应值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean hSet(String key,Object item,Object value,long time){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},item:{},value:{}",key,item,value,ex);
        }
        return false;
    }

    /**
     * 入键值对到map缓存（hash表）
     * @param key redis键
     * @param item map中键
     * @param value map中item对应值
     * @return 执行结果 true成功，false失败
     */
    public Boolean  hSet(String key,Object item,Object value){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},item:{},value:{}",key,item,value,ex);
        }
        return false;
    }

    /**
     * 获取key对应map的item键值（hash表）
     * @param key redis键
     * @param item map键
     * @return key对应map的item键值
     */
    public Object hGet(String key,Object item){
        try {
            return redisTemplate.opsForHash().get(key,item);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{}",key,ex);
        }
        return null;
    }

    /**
     * 获取key对应整个map（hash表）
     * @param key redis键
     * @return key对应整个map
     */
    public Map<Object,Object> hmGet(String key){
        try {
            return key == null ? Collections.emptyMap() : redisTemplate.opsForHash().entries(key);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{}",key,ex);
        }
        return null;
    }

    /**
     *  删除key对应map中某些键值（hash表）
     * @param key redis键
     * @return items map<item,value>
     */
    public void hDel(String key,Object... items){
        redisTemplate.opsForHash().delete(key, items);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hIncr(String key, Object item, long by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hIncr(String key, Object item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hDecr(String key, Object item, long by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * hash递减
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hDecr(String key, Object item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, Object item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    //==========================链表list==============================
    /**
     * 从右边添加list（链表list）
     * @param key redis键
     * @param list list链表
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public <T> Boolean llSet(String key, List<T> list, long time){
        try {
            redisTemplate.opsForList().rightPushAll(key,list);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,list,ex);
        }
        return false;
    }

    /**
     * 从右边添加list（链表list）
     * @param key redis键
     * @param list list链表
     * @return 执行结果 true成功，false失败
     */
    public <T> Boolean llSet(String key, List<T> list){
        try {
            redisTemplate.opsForList().rightPushAll(key,list);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,list,ex);
        }
        return false;
    }

    /**
     * 从右边添加value到list（链表list）
     * @param key redis键
     * @param value value
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean lSet(String key, Object value,Long time){
        try {
            redisTemplate.boundListOps(key).rightPush(value);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 从右边添加value到list（链表list）
     * @param key redis键
     * @param value value
     * @return 执行结果 true成功，false失败
     */
    public Boolean lSet(String key, Object value){
        try {
            redisTemplate.boundListOps(key).rightPush(value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 获取链表中索引起止位置段（链表list）
     * @param key redis键
     * @param start 开始位置
     * @param end 结束位置
     * @return key对应map的item键值
     */
    public Object lGet(String key,long start,long end){
        try {
            return redisTemplate.boundListOps(key).range(start,end);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{},start:{},end:{}",key,start,end,ex);
        }
        return null;
    }

    /**
     * 通过索引 获取list中的值（链表list）
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.boundListOps(key).index(index);
        } catch (Exception ex) {
            log.error("获取redis缓存list失败。key:{}",key,ex);
        }
        return null;
    }

    /**
     * 获取链表长度（链表list）
     * @param key redis键
     * @return key对应map的item键值
     */
    public Long lSize(String key){
        try {
            return redisTemplate.boundListOps(key).size();
        }catch (Exception ex) {
            log.error("获取redis缓存list长度失败。key:{}",key,ex);
        }
        return 0L;
    }

    /**
     * 根据索引修改list中的某条数据（链表list）
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 执行结果
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.boundListOps(key).set(index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除列表中值为value的元素，总共删除count次(不常用)
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long index, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key,index,value);
            return Long.valueOf(remove);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 删除list首尾，只保留 [start, end] 之间的值，闭区间
     * @param key   键
     * @param start 索引开始
     * @param end 索引结束
     * @return 移除的个数
     */
    public void lTrim(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key,start,end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    //==========================无序Set==============================
    /**
     * 新增无序Set（无序Set）
     * @param key redis键
     * @param set set
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Set<Object> set, long time){
        try {
            redisTemplate.opsForSet().add(key,set);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},set:{}",key,set,ex);
        }
        return false;
    }

    /**
     * 新增无序Set（无序Set）
     * @param key redis键
     * @param set set
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Set<Object> set){
        try {
            redisTemplate.opsForSet().add(key,set);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},set:{}",key,set,ex);
        }
        return false;
    }

    /**
     * 往Set添加value（无序Set）
     * @param key redis键
     * @param value 值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Object value, long time){
        try {
            redisTemplate.boundSetOps(key).add(value);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 往Set添加value（无序Set）
     * @param key redis键
     * @param value 值
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Object value){
        try {
            redisTemplate.boundSetOps(key).add(value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 获取set缓存的长度（无序Set）
     * @param key 键
     * @return
     */
    public Long sSize(String key) {
        try {
            return redisTemplate.boundSetOps(key).size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 获取set （无序Set）
     * @param key 键
     * @return set
     */
    public Object sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 移除set中值为value的（无序Set）
     * @param key    键
     * @param set 值 可以是多个
     */
    public <T> long sRemove(String key, Set<T> set) {

        Long remove = redisTemplate.boundSetOps(key).remove(set);
        return remove;
    }

    //==========================有序zSet==============================
    /**
     * 入zset缓存（有序zSet）
     * @param key redis键
     * @param value item
     * @param sort 排序
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean zsSet(String key, Object value,long sort,long time){
        try {
            redisTemplate.opsForZSet().add(key,value,sort);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 获取zset缓存的长度（有序zSet）
     * @param key redis键
     * @return
     */
    public long zsSize(String key) {
        try {
            return redisTemplate.boundZSetOps(key).size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 加减分（有序zSet）
     * @param key  redis键
     * @param value 值
     * @param sore 加分数
     * @return  加后分数
     */
    public <T>  Double zsIncr(String key,T value,long sore) {
        return  redisTemplate.boundZSetOps(key).incrementScore(value, sore);
    }

    /**
     * 获取Zset中某项分数（有序zSet）
     * @param key  redis键
     * @param value 值
     */
    public <T> Double zsGetScore(String key,T value) {
        return  redisTemplate.boundZSetOps(key).score(value);
    }

    /**
     * 获取zset （有序zSet）
     * @param key 键
     * @return set
     */
    public Object zsGet(String key,long min,long max) {
        try {
            //从小到大
            return redisTemplate.opsForZSet().rangeByScore(key,min,max);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 移除zset中值为value的（有序zSet）
     * @param key    键
     * @param set 值 可以是多个
     */
    public <T> long zsRemove(String key, Set<T> set) {
        Long remove = redisTemplate.boundZSetOps(key).remove(set);
        return remove;
    }
}

```
#### 2.4 启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//扫描到配置的redis工具类与配置类及其他需要spring管理的bean
@ComponentScan(basePackages={"com.zlk"})
//需要排除阿里默认数据源，否则启动报错
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
```

#### 2.5 Redis缓存测试工具类

```java
import com.zlk.common.redis.util.RedisUtil;
import com.zlk.core.model.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author likuan.zhou
 * @title: IUserServiceTest
 * @projectName common-test
 * @description: redis测试
 * @date 2021/9/15/015 19:37
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisUtilTest {

    //过期时间
    private final static Long EXPIRE_TIME = 60*60L;
    //kye分割使用:会按文件夹方式分割归类。
    //字符串类型key（key:String）
    private final static String STR_KEY = "str:key";
    private final static String STR_KEY2 = "str:key2";
    //hash(map)类型key（key:Map<item,Object>）,其中map的item通常为Map中对象的主键id.
    private final static String HASH_KEY = "hash:key";
    private final static String HASH_LONG_KEY = "hash:long:key";
    //list链表类型key（key:list||单个对象）
    private final static String LIST_KEY = "list:key";
    //set链表类型key（key:）
    private final static String SET_KEY = "set:key";
    //zset链表类型key（key:）
    private final static String ZSET_KEY = "zset:key";
    private final static Long SIZE = 5L;
    @Autowired
    private RedisUtil redisUtil;
    //================redis-公用方法===================
    @Test
    public void testDel() {
        //删除单个key
        Boolean bl = redisUtil.del(STR_KEY);
        assertNotNull(bl);
    }

    @Test
    public void testDelS() {
        // 批量删除key
        Set<String> keySet  = new HashSet<>();
        keySet.add(STR_KEY);
        keySet.add(STR_KEY2);
        redisUtil.del(keySet);
    }

    //================redis-字符串类型(最常用类型)===================
    // key 对应String字符串，所有操作都要对整个String字符串对象。
    @Test
    public void testSet() {
        //redisUtil.set(STR_KEY2,getUserList(),EXPIRE_TIME);
        redisUtil.set(HASH_KEY,getUserList(),EXPIRE_TIME);
    }

    @Test
    public void testGet() {
        List<UserVO> userVOList = (List<UserVO>)redisUtil.get(STR_KEY);
        assertNotNull(userVOList);
    }

    //================redis-Hash类型（map）===================
    // 以下为 key:map<item,userVO> 。将list转为list.size()个map集合。
    // list<UserVO> ==>Map<"id",UserVO>==>key:map<item,userVO>
    // 适合存对象集合列表，不同于String需要操作整个对象，hash可以对特定一条数据进行操作（单条增删改查）。也可以对整个key进行操作。
    // 等同于java的HashMap
    @Test
    public void testHmSet() {
        // 入整个map
        List<UserVO> userList = getUserList();
        Map<Long, UserVO> userVOMap = userList.stream().collect(Collectors.toMap(UserVO::getId, userVO -> userVO));
        Boolean bl = redisUtil.hmSet(HASH_KEY, userVOMap, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testHmGet() {
        // 获取key对应整个map
        Map<Object, Object> map = redisUtil.hmGet(HASH_KEY);
        Map<Object, UserVO> userVOMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> (UserVO) v.getValue()));
        assertNotNull(userVOMap);
    }

    @Test
    public void testHSet() {
        // 对应key,对map<item,value>入一个键值对（存在更新，不存在则新增）
        List<UserVO> userList = getUserList();
        UserVO userVO =  new UserVO();
        userVO.setId(6L);
        userVO.setName("name:"+6);
        userVO.setStatus(1);
        Boolean bl = redisUtil.hSet(HASH_KEY, 6L,userVO, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testHGet() {
        // 对应key,获取map<item,value>的item键值。
        // 如map<5,value>,获取key中item=5的value
        UserVO userVO = (UserVO)redisUtil.hGet(HASH_KEY, 6L);
        assertNotNull(userVO);
    }

    @Test
    public void testHDel() {
        //删除key对应map中某些键值（hash表）
        redisUtil.hDel(HASH_KEY, 5,6);
    }

    @Test
    public void testHIncr() {
        //递增
        Map<Long,Object> map = new HashMap<>();
        map.put(1001L,10);
        redisUtil.hmSet(HASH_LONG_KEY,map,EXPIRE_TIME);
        redisUtil.hIncr(HASH_LONG_KEY, 1001L,6);
        // 结果map.put(1001L,16);
    }

    @Test
    public void testHHasKey() {
        //判断key对应map<item,value>中是否存在item键。
        // 存在未true，不存在为false
        boolean bl = redisUtil.hHasKey(HASH_KEY, 5L);
        assertEquals(true,bl);
    }

    //================redis-List类型（链表）===================
    // 不管是否存在对应值，链表每次新增都会占且只占一个索引位（不管一次插入多条还是一条）。
    // 效果等同于java的linkedList
    // 下标0为开始，1为第二。-1为结束，-2为倒数第二。
    @Test
    public void testLlSet() {
        // 从右开始插入一个list到redis链表(只会整个list占链表一个索引位置，再增加后不包含在当前list中，而是另外占一个索引位置。慎用)
        // 如执行 testLlSet() 两次。则LIST_KEY[0]有六条数据，则LIST_KEY[1]也会生成六条数据
        List<UserVO> userList = getUserList();
        Boolean bl = redisUtil.llSet(LIST_KEY, userList, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testLSet() {
        // 从右开始插入一条数据到redis链表(之前的list占链表一个位置，再增加后不包含在当前list中，自己占一个链表位置。慎用)
        // 例如：之前testLlSet()方法执行后6条数据占链表的0索引位置。而testLSet()执行的一条数据占链表索引1位置。明显不是我们想要的结果。
        UserVO userVO = new UserVO();
        userVO =  new UserVO();
        userVO.setId(7L);
        userVO.setName("name:"+7);
        userVO.setStatus(1);
        Boolean bl = redisUtil.lSet(LIST_KEY, userVO, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testLlGet() {
        //获取key对应索引对应的整个list
        //索引  index>=0时， 0 表头，1 第二个元素，依次类推；
        //index<0时，-1，表尾，-2倒数第二个元素，依次类推
        List<UserVO> userList = (List<UserVO>) redisUtil.lGet(LIST_KEY, 0, -1);
        for (int i = 0; i < userList.size(); i++) {
            System.out.println("索引下标："+i+"。索引对应value:"+userList.get(i));
        }
        //执行testLlSet()，执行testLSet()，执行testLlSet()做新增后结果如下：
        //索引下标：0。索引对应value:[UserVO(id=1, name=name:1, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=2, name=name:2, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=3, name=name:3, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=4, name=name:4, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=5, name=name:5, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null)]
        //索引下标：1。索引对应value:UserVO(id=7, name=name:7, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null)
        //索引下标：2。索引对应value:[UserVO(id=1, name=name:1, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=2, name=name:2, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=3, name=name:3, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=4, name=name:4, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=5, name=name:5, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null)]
        assertEquals(3,userList.size());
    }

    @Test
    public void testLGetIndex() {
        //获取key对应索引对应的单个值
        //索引  index>=0时， 0 表头，1 第二个元素，依次类推；
        //index<0时，-1，表尾，-2倒数第二个元素，依次类推
        List<UserVO> userList = (List<UserVO>) redisUtil.lGetIndex(LIST_KEY, 0);
        assertNotNull(userList);
    }

    @Test
    public void testLSize() {
        //获取key对应索引长度
        long size = redisUtil.lSize(LIST_KEY);
        assertEquals(3,size);
    }

    @Test
    public void testLUpdateIndex() {
        //更新key对应索引1下标所有数据（覆盖）。（1下标为第二条）
        List<UserVO> userList = getUserList();
        boolean bl = redisUtil.lUpdateIndex(LIST_KEY, 1, userList);
        assertEquals(true,bl);
    }

    // lRemove
    @Test
    public void testLTrim() {
        //删除链表数据按下标（0为开始，-1为最后）
        // 删除[0,1]两条数据
        redisUtil.lTrim(LIST_KEY, 0, 1);
    }

    //================redis-set类型（无序集合）===================
    //类似java的HashSet，不会存在重复数据
    @Test
    public void testSSet() {
        // 插入SET,类似java的HashSet，不会存在重复数据（只在当前插入批次去重）
        // 以下只会存在一个set
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean = redisUtil.sSet(SET_KEY, userVO, EXPIRE_TIME);
        userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean2 = redisUtil.sSet(SET_KEY, userVO, EXPIRE_TIME);
        assertEquals(true,aBoolean);
    }

    @Test
    public void testSSet2() {
        // 该方法只适合每次都操作整个key对应SET.
        // 插入SET,类似java的HashSet，不会存在重复数据
        // 以下结果set1与set3一样只会存在一个set,且只有1001与1002
        // set2会自己存在一个Set,数据1001,1002,1004
        Set<String> set1 = new HashSet<>();
        set1.add("1001");
        set1.add("1001");
        set1.add("1002");
        Boolean aBoolean = redisUtil.sSet(SET_KEY, set1, EXPIRE_TIME);
        Set<String> set2 = new HashSet<>();
        set2.add("1001");
        set2.add("1003");
        set2.add("1004");
        Boolean aBoolean2 = redisUtil.sSet(SET_KEY, set2, EXPIRE_TIME);
        Set<String> set3 = new HashSet<>();
        set3.add("1001");
        set3.add("1001");
        set3.add("1002");
        Boolean aBoolean3 = redisUtil.sSet(SET_KEY, set3, EXPIRE_TIME);
        assertEquals(true,aBoolean);
    }

    @Test
    public void testSGet() {
        // 获取key对应set
        Object sGet = redisUtil.sGet(SET_KEY);
        assertNotNull(sGet);
    }

    @Test
    public void testSSize() {
        // 获取key对应set的size
        long aLong = redisUtil.sSize(SET_KEY);
        assertEquals(3L,aLong);
    }

    @Test
    public void testSRemove() {
        // 移除key对应某些set
        Set<String> set = new HashSet<>();
        set.add("1001");
        set.add("1002");
        //移出的整个set(1001,1002)内容。
        long aLong = redisUtil.sRemove(SET_KEY,set);
        assertEquals(3L,aLong);
    }

    //================redis-zset类型（有序集合）===================
    //类似java的HashSet，不会存在重复数据，但是有分值比重。
    // 适合排名数据（如排行榜等热数据）
    @Test
    public void testZsSet() {
        // 插入SET,类似java的HashSet，不会存在重复数据（只在当前插入批次去重）
        // 以下只会存在2个set(后两条存在，第二条会覆盖第一条).且有比分排名
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean = redisUtil.zsSet(ZSET_KEY, userVO, 1,EXPIRE_TIME);
        userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean2 = redisUtil.zsSet(ZSET_KEY, userVO,2, EXPIRE_TIME);
        userVO = new UserVO();
        userVO.setId(11L);
        userVO.setName("name:"+11L);
        userVO.setStatus(1);
        Boolean aBoolean3 = redisUtil.zsSet(ZSET_KEY, userVO,3, EXPIRE_TIME);
        assertEquals(true,aBoolean);
    }

    @Test
    public void testZsGet() {
        //获取key对应Zset。按分值范围。[1,3]
        Set<UserVO> userVOSet = (Set<UserVO>)redisUtil.zsGet(ZSET_KEY,1,3);
        assertNotNull(userVOSet);
    }

    @Test
    public void testZsSize() {
        //获取key对应Zset长度
        long aLong = redisUtil.zsSize(ZSET_KEY);
        assertEquals(3L,aLong);
    }

    @Test
    public void testZsRemove() {
        //获取key对应Zset长度
        Set<UserVO> set = new HashSet<>();
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        set.add(userVO);
        long aLong = redisUtil.zsRemove(ZSET_KEY,set);
        assertEquals(3L,aLong);
    }

    @Test
    public void testZsGetScore() {
        //获取key对应value的分数
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Double score = redisUtil.zsGetScore(ZSET_KEY, userVO);
        System.out.println(score);
    }

    @Test
    public void testZsIncr() {
        //对key对应value的分数做加减
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Double score = redisUtil.zsIncr(ZSET_KEY, userVO,8L);
        System.out.println(score);
    }


    //================造数据===================
    private static List<UserVO> getUserList(){
        ArrayList<UserVO> arrayList = new ArrayList<>();
        UserVO userVO;
        for (long i = 1L; i <= SIZE; i++) {
            userVO =  new UserVO();
            userVO.setId(i);
            userVO.setName("name:"+i);
            userVO.setStatus(1);
            arrayList.add(userVO);
        }
        return arrayList;
}
```
#### 2.5 Redis缓存在项目中使用流程

一般会用于用户与数据库中间做一层redis缓存。用于提高并发QPS,降低对关系型数据库的访问压力。

-- 适用场景

    访问并发高，共享型（也不能量大,内存有限），数据少且变动少的数据适合存放到Redis。如分布式共享的token、数据字典、地区、热门的排行榜数据、分布式锁标记等。

-- 注意事项

    1.由于redis是内存型非关系型数据库，内存贵且大小限制强。所以不要把什么数据都存入Redis,
      所有数据非必要需要设置过期时间。若非必要必需使用无过期时间的数据存放。

    2.key命名尽量短，且用“：”分割（key如 项目名称:user:userId），方便分门别类。大部分情况使用String类型即可。

应用缓存业务流程图

![Image text](./images/redis应用缓存.jpg)

redis缓存在项目中见项目：

    可以使用注解或者调用方法实现同样的效果，下面演示调用方法实现。


### 参考

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis
    
    springboot Redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    Redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693

    Redis数据结构： https://zhuanlan.zhihu.com/p/344918922

    跳跃表：https://www.jianshu.com/p/c2841d65df4c