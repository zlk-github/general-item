## Linux生产问题排查

    常见线上故障CPU、磁盘、内存、网络等问题排查。

    基本上出问题就是 df、free、top 三连，然后依次 jstack、jmap 伺候，具体问题具体分析即可。

### 1 CPU

常见于业务逻辑问题（死循环）、频繁GC、以及上下文切换过多等造成。

#### 1.1 top 或者 ps

    cpu整体占用情况
        mpstat -P ALL 1

    1. 找到对应的pid
        ps命令找到对应进程PID
           找到java进程：ps aux | grep java
        使用top查看占用高的进程。
          所有进程cpu使用情况：top 
          查看java进程使用内存与cpu情况：top -b -n 1 | grep java| awk '{print "PID:"$1",mem:"$6",CPU percent:"$9"%","mem percent:"$10"%"}'
    
    2.找到对应CPU使用较高的PID
        top -H -p pid

    3.将占用最高的 pid 转换为 16 进制 printf '%x\n' pid 得到 nid  
         printf '%x\n' pid 得到 nid

cpu整体占用情况：mpstat -P ALL 1  （表示每1秒产生一个报告）

```java
root@VM-4-5-ubuntu:/usr/local/rocketmq# mpstat -P ALL 1
Linux 4.15.0-159-generic (VM-4-5-ubuntu) 	05/10/2022 	_x86_64_	(2 CPU)

09:58:11 AM  CPU    %usr   %nice    %sys %iowait    %irq   %soft  %steal  %guest  %gnice   %idle
09:58:12 AM  all    1.00    0.00    1.99    0.00    0.00    0.00    0.00    0.00    0.00   97.01
09:58:12 AM    0    0.99    0.00    2.97    0.00    0.00    0.00    0.00    0.00    0.00   96.04
09:58:12 AM    1    0.00    0.00    2.02    0.00    0.00    0.00    0.00    0.00    0.00   97.98

09:58:12 AM  CPU    %usr   %nice    %sys %iowait    %irq   %soft  %steal  %guest  %gnice   %idle
09:58:13 AM  all    2.04    0.00    0.51    0.00    0.00    0.00    0.00    0.00    0.00   97.45
09:58:13 AM    0    1.01    0.00    1.01    0.00    0.00    0.00    0.00    0.00    0.00   97.98
09:58:13 AM    1    4.04    0.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00   95.96
```

#### 1.2 jstack 分析 CPU 问题

    使用nid查询堆栈消息
    jstack pid |grep 'nid' -C5 –color：

    也可以生成 jstack 文件进行分析。一般关注 WAITING 和 TIMED_WAITING 的部分，BLOCKED 就不用说了。
    然后对nid出现的问题WAITING与TIMED_WAITING部分进行grep -i '问题'；查看如果WAITING 之类的特别多，那么多半是有问题。

#### 1.3 频繁 GC

    jstat -gc pid 1000 命令来对 GC 分代变化情况进行观察，1000 表示采样间隔（ms），S0C/S1C、S0U/S1U、EC/EU、OC/OU、MC/MU 分别代表两个 Survivor 区、Eden 区、老年代、元数据区的容量和使用量。
    YGC/YGT、FGC/FGCT、GCT 则代表 YoungGc、FullGc 的耗时和次数以及总耗时。
    如果看到 GC 比较频繁，再针对 GC 方面做进一步分析

#### 1.4 上下文切换

    vmstat 命令来进行查看
        cs（context switch）一列则代表了上下文切换的次数。如果我们希望对特定的 pid 进行监控那么可以使用 pidstat -w pid 命令，
        cswch 和 nvcswch 表示自愿及非自愿切换。


### 2 磁盘

    磁盘整体使用情况：
        df -hl

    当前目录下子目录和文件的大小：
        du -sh *

    查看磁盘性能问题：
        iostat -d -k -x
        最后一列 %util 可以看到每块磁盘写入的程度，而 rrqpm/s 以及 wrqm/s 分别表示读写速度

    查看pid磁盘读写情况
        lsof -p pid
    查看指定端口占用情况
        lsof -i:8081
    或者cat /proc/pid/io
    注： lsof命令查看，需要安装，yum install lsof -y

其中iostat：

    Iostat提供三个报告：CPU利用率、设备利用率和网络文件系统利用率，使用-c，-d和-h参数可以分别独立显示这三个报告。


### 3 内存

    主要包括 OOM、GC 问题和堆外内存。

#### 内存分析命令：free m

    内存整体使用情况
        free
        free m 
        free -h

### 4 网络超时


    
    
### 参考
    
    https://mp.weixin.qq.com/s/eyGQW-ErjRH-uGu51GUlTA