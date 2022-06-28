###  Linux 安装 Redis

    Linux：conts7
    
    Redis版本: 5.0.14
        官网下载网页链接：https://redis.io/download
    
    第三方Redis连接工具推荐：RedisDesktopManager
        官网下载：https://redisdesktop.com/download

### 1 Redis 下载

官网下载网页链接：https://redis.io/download

![Image text](./images/redis下载地址.png)

### 2 上传到linux路径 /usr/local/redis

    cd /usr/local/redis
    rz命令上传

### 3 解压redis-5.0.14.tar.gz

    tar -zxvf redis-5.0.14.tar.gz
    
    解压目录结果如下（redis-5.0.14）
    [root@iZwz9feedi7zuvp1dofbzhZ redis]# ls
    redis-5.0.14  redis-5.0.14.tar.gz  redis-6.2.6.tar.gz

### 4 安装gcc环境

Redis是C语言编写，需要安装C环境。

    yum install gcc-c++

### 5 进入/usr/local/redis/redis-5.0.14下进行编译与安装

    进入目录： cd /usr/local/redis/redis-5.0.14
    编译： make
    当前目录切换到src： cd ./src
    安装Redis： make install

### 5 为了方便管理，将Redis文件中的conf配置文件和常用命令移动到统一文件中

     进入目录： cd /usr/local/redis/redis-5.0.14

#### 5.1 创建bin目录与etc目录

    创建bin目录：mkdir bin  
    创建etc目录：mkdir etc

#### 5.2 将redis-5.0.14目录下的 redis.conf与sentinel.conf移动到 redis-5.0.14目录下的etc文件夹下。

    移动: mv redis.conf ./etc/
    移动：mv sentinel.conf ./etc/

#### 5.3 将mkreleasehdr.sh、redis-benchmark、redis-check-aof、redis-cli、redis-server 移动到   /usr/local/redis/redis-5.0.14//bin/ 目录下.

    当前切换目录到src： cd ./src/
    移动:  mv mkreleasehdr.sh redis-benchmark redis-check-aof redis-cli redis-server redis-sentinel /usr/local/redis/redis-5.0.14/bin/

Redis相关可执行文件的主要作用

    （1）redis-server  -------Redis服务器
    （2）redis-cli         -------Redis命令行客户端
    （3）redis-benchmark ---------Redis性能测试工具
    （4）redis-check-aof ----------AOF文件修复工具
    （5）redis-check-dump --------RDB文件检查工具

### 6 编辑 redis.conf配置文件，设置后台启动redis服务，开启redis远程访问服务，允许远程访问，更改密码。

    进入etc目录：/usr/local/redis/redis-5.0.14/etc
    编辑Redis配置文件：
    1.打开文件：vim redis.conf
    2.进入编辑：i

    3.daemonize yes （daemonize yes表明需要在后台运行）
    --4.# bind 127.0.0.1 （bind 127.0.0.1 这一行给注释掉，这里的bind指的是只有指定的网段才能远程访问这个redis，注释掉后，就没有这个限制了。）
    4. bind 0.0.0.0 （127.0.0.1只有本地可以访问，0.0.0.0则都可以访问）
    5.protected-mode no（protected-mode no允许远程访问）
    6.# requirepass foobared （去掉注释，更改密码，看需要设置，默认不设置密码为空）

    7.退出编辑:
        esc退出编辑状态。
        ：wq 保存退出
        ：q！强制退出，所有改动不生效（该处未使用到）

### 7 设置Redis开机启动

将Redis启动与配置路径添加到系统 /etc/rc.d/rc.local后保存。

    进入编辑： vim /etc/rc.d/rc.local
        添加如下内容（redis的bin目录与配置文件目录）：
        /usr/local/redis/redis-5.0.14/bin/redis-server  /usr/local/redis/redis-5.0.14/etc/redis.conf

### 8 启动Redis

#### 前置条件关闭防火墙或者防火墙已放行该端口。

    防火墙状态：systemctl status firewalld
    开启防火墙 ：systemctl start firewalld
    开机自启：systemctl enable firewalld
    防火墙已放行端口6379 ：
        （1）如我们需要开启XShell连接时需要使用的6379端口
        firewall-cmd --zone=public --add-port=6379/tcp --permanent
        其中--permanent的作用是使设置永久生效，不加的话机器重启之后失效
        （2）重新载入一下防火墙设置，使设置生效
        firewall-cmd --reload
        （3）可通过如下命令查看是否生效
        firewall-cmd --zone=public --query-port=6379/tcp


该处不需要执行：

    查看当前系统打开的所有端口:firewall-cmd --zone=public --list-ports
    关闭防火墙（暂时）:systemctl stop firewalld
    关闭防火墙（永久）:systemctl disable firewalld
    防火墙详情：firewall-cmd --list-all
    限制端口:
        （1）比如我们现在需要关掉打开的22端口
        firewall-cmd --zone=public --remove-port=22/tcp --permanent
        或者firewall-cmd --zone=public --add-port=22/tcp --permanent
        （2）重新载入一下防火墙设置，使设置生效
        firewall-cmd --reload

    批量开放或限制端口
        （1）批量开放端口，如从100到500这之间的端口我们全部要打开
        firewall-cmd --zone=public --add-port=100-500/tcp --permanent
        （2）重新载入一下防火墙设置，使设置生效
        firewall-cmd --reload

#### 8.1 选择配置文件启动Redis

切换到 /usr/local/redis/redis-5.0.14/bin/ 目录下执行 redis-server 命令，使用 /usr/local/redis/redis-5.0.14/etc/redis.conf配置文件来启动redis服务
 
    进入目录：  cd /usr/local/redis/redis-5.0.14/bin
    启动服务命令： ./redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf

#### 8.2 查看Redis启动情况

命令：ps aux|grep redis

    [root@iZwz9feedi7zuvp1dofbzhZ bin]# ps aux|grep redis
    polkitd   4490  0.1  0.1  55528 10164 pts/1    Ssl+ May28 213:12 redis-server *:6379
    root      9997  0.0  0.1 154012  9540 ?        Ssl  09:02   0:00 ./redis-server *:6379
    root     10002  0.0  0.0 112824  2232 pts/0    S+   09:02   0:00 grep --color=auto redis
    [root@iZwz9feedi7zuvp1dofbzhZ bin]#

    netstat -antp | grep 6379


#### 8.3 Redis服务关闭与重启

    进入目录：  cd /usr/local/redis/redis-5.0.14/bin
    停止服务：  ./redis-cli -p 6379 shutdown   (注：不要使用kill -9 PID,会导致备份丢数据)
    停止服务（密码123456）： redis-cli -a 123456 shutdown
    启动服务命令： ./redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf

#### 8.4 开启客户端

    进入目录：  cd /usr/local/redis/redis-5.0.14/bin
    启动客户端命令： ./redis-cli 

客户端进入后如下：（当前窗口可以操作redis命令）

    [root@iZwz9feedi7zuvp1dofbzhZ bin]# ./redis-cli
    127.0.0.1:6379>

### 9 如果是阿里云服务器需要开放6379端口，检查是否设置IP白名单。（--待补充）

安全组配置见：https://help.aliyun.com/document_detail/25471.html?spm=a2c6h.13066369.0.0.45b56c86u5ESFx&userCode=28kqeewo
           https://help.aliyun.com/document_detail/25443.html

**踩坑**：

需要将服务器加入安全组。

![Image text](./images/阿里云加入安全组.png)


### 10 检查外部是否可连接上（开发测试使用）（--待补充）

注：连接需要使用外网IP与开放的端口。

    windows测试远程主机和Redis端口： telnet ip 6379
    或者使用RedisDesktopManager连接测试



### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis

    配置阿里redis云端口：https://www.cnblogs.com/klayHuang/p/14904862.html

    安全组配置端口（详细）：https://help.aliyun.com/document_detail/25471.html?spm=a2c6h.13066369.0.0.45b56c86u5ESFx&userCode=28kqeewo

    LINUX现在ip与端口（详细）：https://blog.csdn.net/ywd1992/article/details/80401630