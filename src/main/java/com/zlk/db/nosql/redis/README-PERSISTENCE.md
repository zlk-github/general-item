##  Redis持久化化

### Redis持久化介绍

由于Redis数据存放在内存中，定期写入磁盘（半持久化）。如果没有配置持久化，redis重启后数据就全丢失了。Redis提供了两种持久化机制，将数据保存到磁盘上，重启后从磁盘恢复数据。
分布为RDB(Redis DataBase)和AOF(Append Only File)。

**RDB**：快照方式，全量备份。将Reids在内存中的数据库记录定时dump到磁盘上的RDB持久化。

**AOF**：日志记录方式。操作日志记录以追加方式写入文件。

|  技术选型  | RDB | AOF |
|  ----  | ----  |----  | 
| 启动优先级| 低  | 高 | 
|  体积 | 小| 大| 
|  恢复速度 | 快| 慢| 
|  数据安全性 | 丢数据 | 根据策略决定 | 
|  轻重 | 重 | 轻| 


### Redis持久化RDB（Redis默认）

    save 900 1     #900秒内如果超过1个key被修改，则发起快照保存
    save 300 10    #300秒内容如超过10个key被修改，则发起快照保存
    save 60 10000

（做容灾备份，对数据丢失容忍高的情况）

    快照方式（版本），全量备份。备份时间为某个时间点数据。
    
    适用于容灾，间隔时间长未来得及备份会导致数据丢失。
    
    RDB 在恢复大数据集时的速度比 AOF 的恢复速度要快。

### Redis持久化AOF

    appendonly yes              //启用aof持久化方式
    # appendfsync always      //每次收到写命令就立即强制写入磁盘，最慢的，但是保证完全的持久化，不推荐使用
    appendfsync everysec     //每秒钟强制写入磁盘一次，在性能和持久化方面做了很好的折中，推荐
    # appendfsync no    //完全依赖os，性能最好,持久化没保证

（做类似实时备份，对数据丢失容忍低的情况，效率低于RDB）

    日志记录方式。操作日志记录以追加方式写入文件。实时追加操作日志文件（默认策略1/s）,故障也只会丢一秒的数据。
    
    Redis 可以在 AOF 文件体积变得过大时，自动地在后台对 AOF 进行重写： 重写后的新 AOF 文件包含了恢复当前数据集所需的最小命令集合。
    
    AOF 文件的体积通常要大于 RDB 文件的体积。

### RDB 和 AOF 选择

    一般来说,如果想达到足以媲美 PostgreSQL 的数据安全性， 你应该同时使用两种持久化功能。
    如果你非常关心你的数据,但仍然可以承受数分钟以内的数据丢失， 那么你可以只使用 RDB 持久化。
    有很多用户都只使用 AOF 持久化， 但我们并不推荐这种方式： 因为定时生成 RDB 快照（snapshot）非常便于进行数据库备份，
    并且 RDB 恢复数据集的速度也要比 AOF 恢复的速度要快， 除此之外， 使用 RDB 还可以避免之前提到的 AOF 程序的 bug 。
    因为以上提到的种种原因， 未来我们可能会将 AOF 和 RDB 整合成单个持久化模型。 （这是一个长期计划。）

### 参考

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis

    Redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556

    Redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693

    Redis持久化区别： https://www.cnblogs.com/shizhengwen/p/9283973.html