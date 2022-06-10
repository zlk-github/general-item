## Linux常用命令


### 1 权限相关

#### 目录说明

#### 目录文件读写权限说明

#### root权限

    sudo 操作命令

###  2 系统级命令

    登录：ssh root@ip
    切换root用户: root su sudo -

    查询登录当前系统的用户信息：w

    查看redis进程：
        ps -ef|grep redis
        ps aux|grep redis

    查看java进程： ps aux|grep java
    
    查看端口占用情况netstat –apn

    杀死进程： kill -9 PID (不推荐使用)

    windows ping远程主机和端口： telnet ip 端口

    磁盘使用情况（整个系统）： 
       df -hl
       结果：Used：已经使用的空间 ，Avail：可以使用的空间 ，Mounted on：挂载的目录

    修改网卡：
        切换目录：cd /etc/sysconfig/network-scripts
        编辑ifcfg-ens33：vi ifcfg-ens33
        重启网卡：service network restart
        vi /etc/sysconfig/network-scripts/ifcfg-ens33

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

        目录下的文件名称包含nginx: find 目录 -iname '*nginx*' 
        
        系统中目录找包含redis: find -iname "*redis*"

    查看当前目录下子目录和文件的大小： du -sh *

#### 3.2 文件相关

    创建文件catalina.out并赋予读写权限：
        touch catalina.out
        chmod 777 catalina.out

    强制删除文件catalina.out：rm -rf catalina.out

    移动catalina.out到catalina2.out：mv catalina.out/ catalina2.out/

    复制catalina.out到catalina2.out(catalina2.out不存在)：cp -r catalina.out/ catalina2.out/
    
    清空文件内容： echo -n "" > /var/logs/msg.log
    
                shell脚本---》 清空
                #!/bin/bash
                echo -n "" > /usr/local/jiaoben/cs1001.log
                echo -n "" > /usr/local/jiaoben/cs1002.log
                授权：chmod 744 /usr/local/shell/clean-log-now.sh
                清除格式： sed -i "s/\r//" /usr/local/shell/clean-log-now.sh
                执行脚本(清除Java应用最新日志，谨慎使用)：./usr/local/shell/clean-log-now.sh
                
              shell脚本---》 删除90天前.log
                    #!/bin/bash
                    find /data/paas/smsa/tomcat-smsp-task/logs -mtime +90 -name ".log" -exec rm -rf {} \;
                    授权：chmod 744 /usr/local/shell/clean-log-history.sh
                    清除格式： sed -i "s/\r//" /usr/local/shell/clean-log-history.sh
                   执行脚本(清除Java90天前日志，C++200天前)：：./usr/local/shell/clean-log-history.sh

    上传下载文件：
        上传：rz （本地上传 sudo scp 本地目录 用户名@ip: 远程目录）
        sudo scp G:\git\exercise_jvm\target\jvm-0.0.1-SNAPSHOT.jar root@122.112.246.108:/data/ 
    
        下载：sz

    # 按文件后缀收缩(1天内有改动) find . -name '*.log'  -mtime -1 -exec  ls -l \;
         find /home/Ask/ -type f -size -10M -exec ls -lh {} \;
         find /root/ -name "*.log"
         按后缀全局匹配：find . -regex ".*\.\(log\)" 
         
    
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
        匹配内容中有2020的行,按后面开始展示100行：grep "2020" -i catalina.out | tail -n100
        最新内容滚动查看：tail -f catalina.out
        最新内容查看500行（在之后更新的将不滚动展示）：tail -n 500 catalina.out
        动态最新内容查看500行（在之后更新的将不滚动展示）：tail -f -n 500 catalina.out

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

#### cpu--top

    top

#### Iostat

    iostat

Iostat提供三个报告：CPU利用率、设备利用率和网络文件系统利用率，使用-c，-d和-h参数可以分别独立显示这三个报告。

#### 内存分析命令：free m

    free m 
    free -h
    
#### CPU占用情况

    mpstat -P ALL 1
   

#### 端口使用情况查看
    lsof命令查看
    需要安装，yum install lsof -y
    查看指定端口占用情况
    lsof -i:8081
 
#### 磁盘使用情况

    
    
  
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

### JAVA 

#### 查看java进程使用内存与cpu情况

    top -b -n 1 | grep java| awk '{print "PID:"$1",mem:"$6",CPU percent:"$9"%","mem percent:"$10"%"}'


PS

    ps -ef         #显示所有当前进程
    ps aux         #显示所有当前进程
    ps -ax         #显示所有当前进程
    ps -u pungki   #根据用户过滤进程
    ps -aux --sort -pcpu | less #根据 CPU 使用来升序排序
    ps -aux --sort -pmem | less #根据用户过滤进程
    ps -aux --sort -pcpu,+pmem | head -n 10 #查询全10个使用cpu和内存最高的应用
    ps -C getty    #通过进程名和PID过滤
    ps -f -C getty #带格式显示的，通过进程名和PID过滤
    ps -L 1213     #根据线程来过滤进程
    ps -axjf（或pstree）   #树形显示进程
    ps -eo pid,user,args  # 显示安全信息


#### 防火墙IP管理命令

#### java

##### 启动命令

    nohup java -jar rocketmq-console-ng-1.0.0.jar --spring.profiles.active=test >out.log 2>&1 &

##### 查看进程

    ps aux | grep java

#### 上传包到私服务

    mvn deploy:deploy-file -DgroupId=groupId -DartifactId=artifactId  -Dversion=版本号  -Dpackaging=jar -Dfile=包地址  
    -Durl=私服仓库地址 -DrepositoryId=私服仓库地址目录 -X

    如：
        mvn deploy:deploy-file -DgroupId=common-zlk -DartifactId=common-zlk-core  -Dversion=1.0.0-SNAPSHOT  -Dpackaging=jar -Dfile=common-zlk-core-1.0.0-SNAPSHOT.jar  
        -Durl=http://127.0.0.1:8099/nexus/content/repositories/snapshots/ -DrepositoryId=snapshots -X

#### c++

./hello

### 参考
    
    阿里云安全组配置端口（详细）：https://help.aliyun.com/document_detail/25471.html?spm=a2c6h.13066369.0.0.45b56c86u5ESFx&userCode=28kqeewo
    
    LINUX现在ip与端口（详细）：https://blog.csdn.net/ywd1992/article/details/80401630

    linux 内存与cpu命令参考： https://blog.csdn.net/weixin_40482816/article/details/118385737