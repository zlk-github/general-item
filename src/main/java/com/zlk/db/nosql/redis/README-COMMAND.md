##  Redis常用命令

Redis相关可执行文件的主要作用

    （1）redis-server  -------Redis服务器
    （2）redis-cli         -------Redis命令行客户端
    （3）redis-benchmark ---------Redis性能测试工具
    （4）redis-check-aof ----------AOF文件修复工具
    （5）redis-check-dump --------RDB文件检查工具

启动Redis集群顺序

    1.启动Redis，顺序主->从
    ./redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf
    
    2.启动Sentinel，顺序主->从
    ./redis-sentinel /usr/local/redis/redis-5.0.14/etc/sentinel.conf

### Redis应用级

    redis/bin目录下：
    
    启动服务命令： ./redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf
    启动服务命令： ./redis-sentinel /usr/local/redis/redis-5.0.14/etc/sentinel.conf
    进入redis-cli： redis-cli
    进入redis-cli(密码123456)： 单机：./redis-cli -h 127.0.0.1 -p 6379 -a 123456
                               集群：./redis-cli -c -h 集群ip -p 端口 -a 密码

    ./redis-cli -c -h 172.19.176.190 -p 7001 -a qA5mBVUuHv8Fc7Uy4Jnf

    查看服务： ps aux|grep redis
    查看端口: netstat -antp | grep 6379
    停止服务： redis-cli -p 6379 shutdown   (注：不要使用kill -9 PID,可能导致备份丢数据)
    停止服务（密码123456）： redis-cli -a 123456 shutdown    (注：不要使用kill -9 PID,可能导致备份丢数据)

    进入redis-cli： redis-cli
         查看集群：info replication

### Redis操作命令

####  开发常用

    查看全部key （不要在代码中使用）
    
        keys *

    匹配key

        key为:apple开头
        keys apple*

        key为:包含apple
        keys *apple*


    (推荐使用，结果可能不精准)scan用法（cursor为游标，标记位置；MATCH为匹配内容;COUNT指定为查询范围，如从1000条中筛选）
         SCAN cursor [MATCH pattern] [COUNT count]
         例：SCAN 0 apple* 1000
    
    key的有效时间
    
        ttl key
            说明：
            key不存在返回-2
            key没设置过期时间返回-1
            key有设置过期时间，返回剩余时间。（单位毫秒）
    
    设置过期时间
        1.EXPIRE＜key＞＜ttl＞命令用于将键key的生存时间设置为ttl秒。
        2.PEXPIRE＜key＞＜ttl＞命令用于将键key的生存时间设置为ttl毫秒。
        3.EXPIREAT＜key＞＜timestamp＞命令用于将键key的过期时间设置为timestamp所指定的秒数时间戳。
        4.PEXPIREAT＜key＞＜timestamp＞命令用于将键key的过期时间设置为timestamp所指定的毫秒数时间戳。

    删除key
    del key

#### 按数据类型



### 参考

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis

    Redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556

    Redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693

    Redis持久化区别： https://www.cnblogs.com/shizhengwen/p/9283973.html