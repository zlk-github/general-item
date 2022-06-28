## Redis集群方案(高可用)之哨兵模式（一主二从三哨兵）

    Linux：conts7

    Redis版本: 5.0.14
        官网下载网页链接：https://redis.io/download
    
    第三方Redis连接工具推荐：RedisDesktopManager
        官网下载：https://redisdesktop.com/download

### 1 一主二从三哨兵介绍

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

### 2 一主二从三哨兵搭建

环境：

|  IP地址  | 端口 | 角色 | Redis版本
|  ----  | ----  |----  | ----  | 
| 192.168.2.203| 6379 |redis-master,sentinel  | 5.0.14 |
| 192.168.2.205| 6379 |redis-slave1,sentinel  | 5.0.14 |
| 192.168.2.206| 6379 |redis-slave2,sentinel  | 5.0.14 |

Redis安装见：[Linux安装Redis教程](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-INIT.md# Linux安装Redis教程)


#### 2.1 配置Redis主节点（redis-master）

##### 2.1.1 主Redis配置文件redis.conf

    vi redis.conf

redis.conf修改内容如下：

```jsx
bind 0.0.0.0            # 表示redis允许所有地址连接。默认127.0.0.1，仅允许本地连接。也可以bind 192.168.2.203
daemonize yes             # 允许redis后台运行
logfile "/var/log/redis.log"    # 设置redis日志存放路径
requirepass "123456"        # 设置redis密码
protected-mode no      # 设置为no，允许外部网络访问
port 6376             # 修改redis监听端口(可以自定义)
pidfile /var/run/redis.pid  # pid存放目录
dir /usr/local/redis/redis-5.0.14/tmp   # 工作目录，需要创建好目录,可自定义
masterauth 123456    # 主从同步master的密码
```
#####  2.1.2 主Redis修改Sentinel(哨兵)配置

    vi sentinel.conf

sentinel.conf修改内容如下：

```java

port 2700 # 修改Sentinel监听端口
daemonize yes  # 允许Sentinel后台运行     
logfile "/var/log/redis-sentinel.log"   # 设置Sentinel日志存放路径
dir /usr/local/redis/redis-5.0.14/tmp   # 工作目录，需要创建好目录,可自定义

# redis01：master名称可自定义
# 192.168.2.203 6379 ：redis主节点IP和端口
# 2 ：表示多少个Sentinel认为redis主节点失效时，才算真正失效
sentinel monitor redis01 192.168.2.203 6379 2    # Sentinel监听redis主节点

# 配置失效时间，master会被这个sentinel主观地认为是不可用的，单位毫秒      
sentinel down-after-milliseconds redis01 10000
        
# 若sentinel在该配置值内未能完成master/slave自动切换，则认为本次failover失败。        
sentinel failover-timeout redis01 60000

# 在发生failover主备切换时最多可以有多少个slave同时对新的master进行同步。
sentinel parallel-syncs redis01 2

# 设置连接master和slave时的密码，注意的是sentinel不能分别为master和slave设置不同的密码，因此master和slave的密码应该设置相同
sentinel auth-pass redis01 123456
```
注：注意：含有mymaster的配置，都必须放置在sentinel monitor mymaster 192.168.2.203 6379 2之后，否则会出现问题


#### 2.2 配置Redis从节点

从节点1（redis-slave1）与从节点2（redis-slave1）

##### 2.2.1 从节点Redis配置文件redis.conf

复制主节点的redis.conf,修改replicaof与bind。

redis-slave1对应redis.conf

    # 表示redis允许所有地址连接。默认127.0.0.1，仅允许本地连接。也可以bind  192.168.2.205 
    bind 0.0.0.0            
    # 主库为主虚拟机1的地址
    replicaof 192.168.2.203 6379

redis-slave2对应redis.conf

    # 表示redis允许所有地址连接。默认127.0.0.1，仅允许本地连接。也可以bind  192.168.2.205 
    bind 0.0.0.0            
    # 主库为主虚拟机1的地址
    replicaof 192.168.2.203 6379
   
#####  2.2.2 从Redis修改Sentinel(哨兵)配置

复制主节点的sentinel.conf即可。需要的话可以改 bind 从节点本机id

    # 表示redis允许所有地址连接。默认127.0.0.1，仅允许本地连接。也可以bind  192.168.2.205 
    bind 0.0.0.0  

### 3 启动Redis

### 3.1 设置Redis开机启动

将Redis启动与配置路径添加到系统 /etc/rc.d/rc.local后保存。

    进入编辑： vim /etc/rc.d/rc.local
        添加如下内容（redis的bin目录启动相关与配置文件目录）：
        /usr/local/redis/redis-5.0.14/bin/redis-server  /usr/local/redis/redis-5.0.14/etc/redis.conf
        /usr/local/redis/redis-5.0.14/bin/redis-sentinel  /usr/local/redis/redis-5.0.14/etc/sentinel.conf

### 3.2 设置软链接，方便启动服务

    ln -s /usr/local/redis/redis-5.0.14/bin/redis-server /usr/bin/redis-server
    ln -s /usr/local/redis/redis-5.0.14/bin/redis-cli /usr/bin/redis-cli
    ln -s /usr/local/redis/redis-5.0.14/bin/redis-sentinel /usr/bin/redis-sentinel

### 3.2 Redis集群启动

    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    启动Redis集群
    1.启动Redis，顺序主->从
    redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf
    
    2.启动Sentinel，顺序主->从
    redis-sentinel /usr/local/redis/redis-5.0.14/etc/sentinel.conf

### 4访问&验证Redis集群


#### 4.1 访问redis主节点(redis-master)

    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    进入redis-cli： redis-cli -h 127.0.0.1 -p 6379 -a 123456
    
    查看集群：info replication
  
master集群结果如下：

```java
  [root@localhost bin]# redis-cli -h 127.0.0.1 -p 6379 -a 123456
  Warning: Using a password with '-a' or '-u' option on the command line interface may not be safe.
  127.0.0.1:6379> info replication
  # Replication
  # 个人注释：当前节点为主节点
  role:master
  # 个人注释：从节点2个，192.168.2.205 6379与192.168.2.206 6379
  connected_slaves:2 
  slave0:ip=192.168.2.206,port=6379,state=online,offset=167962,lag=0
  slave1:ip=192.168.2.205,port=6379,state=online,offset=167684,lag=1
  master_replid:f259bbbd3b0b0c3439460c9e2f666dc68ac6166c
  master_replid2:0000000000000000000000000000000000000000
  master_repl_offset:167962
  second_repl_offset:-1
  repl_backlog_active:1
  repl_backlog_size:1048576
  repl_backlog_first_byte_offset:1
  repl_backlog_histlen:167962
```

#### 4.2 访问redis从节点1(redis-slave1)


    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    进入redis-cli： redis-cli -h 127.0.0.1 -p 6379 -a 123456
    
    查看集群：info replication
  
redis-slave1集群结果如下：

```java
127.0.0.1:6379> info replication
# Replication
 # 个人注释：当前节点为从节点
role:slave
# 个人注释：主节点192.168.2.203 6379
master_host:192.168.2.203
master_port:6379
master_link_status:up
master_last_io_seconds_ago:1
master_sync_in_progress:0
slave_repl_offset:249823
slave_priority:100
# 个人注释：从节点只读
slave_read_only:1
connected_slaves:0
master_replid:f259bbbd3b0b0c3439460c9e2f666dc68ac6166c
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:249823
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:249823
```

#### 4.3 访问redis从节点2(redis-slave2)

    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    进入redis-cli： redis-cli -h 127.0.0.1 -p 6379 -a 123456
    
    查看集群：info replication
  
redis-slave2集群结果如下：

```java
127.0.0.1:6379> info replication
# Replication
 # 个人注释：当前节点为从节点
role:slave
# 个人注释：主节点192.168.2.203 6379
master_host:192.168.2.203
master_port:6379
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:291664
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:f259bbbd3b0b0c3439460c9e2f666dc68ac6166c
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:291664
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:183
repl_backlog_histlen:291482
```
#### 4.4 从节点只读测试(redis-slave2)

哨兵模式：从节点允许读，不允许写入。

    127.0.0.1:6379> set key 123456
    (error) READONLY You can't write against a read only replica.

#### 4.5 主节点写入，复制到从节点

redis-master
    
    127.0.0.1:6379> set key1001 600
    OK

redis-slave1

    127.0.0.1:6379> get key1001
    "600"
    
redis-slave2

    127.0.0.1:6379> get key1001
    "600"

#### 4.6 验证Redis故障转移 

##### 4.6.1 停止主节点192.168.2.203（redis-master）
 
    进入目录：  cd /usr/local/redis/redis-5.0.14/bin
    
    停止服务（密码123456）： redis-cli -a 123456 shutdown    (注：不要使用kill -9 PID,可能导致备份丢数据)


##### 4.6.2 查看主节点（redis-slave1与redis-slave2）

**原来redis-slave1**：

    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    进入redis-cli： redis-cli -h 127.0.0.1 -p 6379 -a 123456
    
    查看集群：info replication

结果如下，192.168.2.205（原来从节点）变为了主节点。从节点只剩下192.168.2.206。
原来主节点192.168.2.203被剔除。

192.168.2.205集群信息。
```java
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=192.168.2.206,port=6379,state=online,offset=18702889,lag=1
master_replid:10dc8efe5dca92037d2cc945fd16a76981afec85
master_replid2:f259bbbd3b0b0c3439460c9e2f666dc68ac6166c
master_repl_offset:18702889
second_repl_offset:18664112
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:17654314
repl_backlog_histlen:1048576
```
**192.168.2.206集群信息：**

    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    进入redis-cli： redis-cli -h 127.0.0.1 -p 6379 -a 123456
    
    查看集群：info replication

结果如下，192.168.2.205（原来从节点1）变为了主节点。
从节点192.168.2.206还是从节点2。

192.168.2.206集群信息。

```java
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:192.168.2.205
master_port:6379
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:18826183
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:10dc8efe5dca92037d2cc945fd16a76981afec85
master_replid2:f259bbbd3b0b0c3439460c9e2f666dc68ac6166c
master_repl_offset:18826183
second_repl_offset:18664112
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:17777608
repl_backlog_histlen:1048576
```

##### 4.6.1 启动192.168.2.203（原来的主节点redis-master）

    进入bin目录(设置软应用不需要)： cd /usr/local/redis/redis-5.0.14/bin
    
    启动服务：redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf

    进入redis-cli： redis-cli -h 127.0.0.1 -p 6379 -a 123456
    
    查看集群：info replication

192.168.2.203（原来主节点）现在变为了从节点。主节点由变成原来的192.168.2.206（原来的从节点）。
```java  
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:192.168.2.205
master_port:6379
master_link_status:up
master_last_io_seconds_ago:2
master_sync_in_progress:0
slave_repl_offset:18780158
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:10dc8efe5dca92037d2cc945fd16a76981afec85
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:18780158
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:18771309
repl_backlog_histlen:8850
```

### 3 Springboot2.0 集成一主二从三哨兵



### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis

    查看： https://www.jianshu.com/p/e71c5a3a7162

