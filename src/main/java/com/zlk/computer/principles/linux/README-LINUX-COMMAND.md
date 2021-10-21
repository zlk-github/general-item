## Linux常用命令


### 1 权限相关

#### 目录说明

#### 目录文件读写权限说明

#### root权限

    sudo 操作命令

###  2 系统级命令

    登录：ssh root@ip
    切换root用户: root su sudo -

    查看redis进程：
        ps -ef|grep redis
        ps aux|grep redis

    查看java进程： ps aux|grep java

    杀死进程： kill -9 PID (不推荐使用)

    windows ping远程主机和端口： telnet ip 端口

    磁盘使用情况（整个系统）： 
       df -hl
       结果：Used：已经使用的空间 ，Avail：可以使用的空间 ，Mounted on：挂载的目录

### 3 目录与文件相关

#### 3.1 目录操作

    切换当前目录下的user目录: cd  usr （切换到根目录：cd / ；往上一级cd ../）

    创建目录redis：mkdir redis

    强制删除目录redis：rm -rf redis

    移动redis到redis1：mv redis/ redis1/

    复制redis到redis1(不存在)：cp -r redis/ redis1/

    查看当前目录路径地址:pwd

    查看当前路径下子目录（只看名称）：ls 

    查看当前路径下子目录（看名称、大小、创建日期与权限）：ls

    查询匹配目录：（未测试）
        查询jdk安装目录（user/bin下）：which java
                        ls -l /usr/bin/java
                        ls -l /etc/alternatives/java

        find 目录 -name '*nginx*' 查找

    查看当前目录下子目录和文件的大小： du -sh *

#### 3.2 文件相关

    创建文件catalina.out并赋予读写权限：
        touch catalina.out
        chmod 777 catalina.out

    强制删除文件catalina.out：rm -rf catalina.out

    移动catalina.out到catalina2.out：mv catalina.out/ catalina2.out/

    复制catalina.out到catalina2.out(catalina2.out不存在)：cp -r catalina.out/ catalina2.out/

    上传下载文件：
        上传：rz （本地上传 sudo scp 本地目录 用户名@ip: 远程目录）
        sudo scp G:\git\exercise_jvm\target\jvm-0.0.1-SNAPSHOT.jar root@122.112.246.108:/data/ 
    
        下载：sz

    解压文件：
        解压tar.gz包 tar -zxvf jdk-8u211-linux-x64.tar.gz
        解压war包jar -xvf game.war

    文件编辑Vim：
        进入文件test.txt：  vim test.txt

        进入编辑:  i

        退出编辑:
            esc然后:
            ：q！强制退出，所有改动不生效
            ：wq 保存退出


    查看日志(一般是日志文件，如下catalina.out是java日志输出文件)：
        匹配内容中有2020的行：grep "2020" -i catalina.out
        最新内容滚动查看：tail -f catalina.out
        最新内容查看500行（在之后更新的将不滚动展示）：tail -n 500 catalina.out

以下命令生产不要使用

    //-----------------------------------关闭Firewalld防火墙
    //1、停止firewalld服务
    systemctl stop firewalld
    //2、禁止firewalld开机启动
    systemctl disable firewalld
    //-----------------------------------关闭SELinux
    //1、临时关闭
    setenforce 0
    //2、永久关闭SELinux
    sed -i "s/^SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config


### 4 性能命令

#### top


### 5 防火墙命令

#### 防火墙

    防火墙状态：systemctl status firewalld
    开启防火墙 ：systemctl start firewalld
    开机自启：systemctl enable firewalld   -- 开启防火墙才会生效
    关闭防火墙（暂时）:systemctl stop firewalld
    关闭防火墙（永久）:systemctl disable firewalld  --使用少
    防火墙详情：firewall-cmd --list-all

#### 防火墙端口管理命令

    查看当前系统打开的所有端口:firewall-cmd --zone=public --list-ports

    防火墙已放行端口6379 ：
            （1）如我们需要开启XShell连接时需要使用的6379端口
            firewall-cmd --zone=public --add-port=6379/tcp --permanent
            其中--permanent的作用是使设置永久生效，不加的话机器重启之后失效
            （2）重新载入一下防火墙设置，使设置生效
            firewall-cmd --reload
            （3）可通过如下命令查看是否生效
            firewall-cmd --zone=public --query-port=6379/tcp

    限制端口:
    （1）比如我们现在需要关掉打开的22端口
    firewall-cmd --zone=public --remove-port=22/tcp --permanent
    （2）重新载入一下防火墙设置，使设置生效
    firewall-cmd --reload

    批量开放或限制端口
        （1）批量开放端口，如从100到500这之间的端口我们全部要打开
        firewall-cmd --zone=public --add-port=100-500/tcp --permanent
        （2）重新载入一下防火墙设置，使设置生效
        firewall-cmd --reload

#### 防火墙IP管理命令


### 参考
    
    阿里云安全组配置端口（详细）：https://help.aliyun.com/document_detail/25471.html?spm=a2c6h.13066369.0.0.45b56c86u5ESFx&userCode=28kqeewo
    
    LINUX现在ip与端口（详细）：https://blog.csdn.net/ywd1992/article/details/80401630
