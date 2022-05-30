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

1）cpu整体占用情况：mpstat -P ALL 1  （表示每1秒产生一个报告）

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
2）找到对应的pid

    1.java进程： ps aux | grep java 

```java
(第二个输出参数为PID)
root@VM-4-5-ubuntu:/usr/local/rocketmq# ps aux | grep java
root     14429  0.0 10.0 2783488 189336 ?      Sl   May09   1:33 /usr/local/jdk1.8.0_202/bin/java -server -Xms256M -Xmx256M -Xmn128M -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8 -XX:-UseParNewGC -verbose:gc -Xloggc:/dev/shm/rmq_srv_gc_%p_%t.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=30m -XX:-OmitStackTraceInFastThrow -XX:-UseLargePages -Djava.ext.dirs=/usr/local/jdk1.8.0_202/jre/lib/ext:/usr/local/rocketmq/rocketmq-4.9.1/bin/../lib:/usr/local/jdk1.8.0_202/lib/ext -cp .:/usr/local/rocketmq/rocketmq-4.9.1/bin/../conf:.:/usr/local/jdk1.8.0_202/lib:/usr/local/jdk1.8.0_202/jre/lib: org.apache.rocketmq.namesrv.NamesrvStartup
root     20823  3.1 15.5 2822940 291940 pts/0  Sl   08:54   0:28 java -Xms128m -Xmx256m -jar rocketmq-dashboard-1.0.1-SNAPSHOT.jar --server.port=8080 --rocketmq.config.namesrvAddr=124.220.189.132:9876
```

    2.查看java进程使用内存与cpu情况：
    top -b -n 1 | grep java| awk '{print "PID:"$1",mem:"$6",CPU percent:"$9"%","mem percent:"$10"%"}'

```java
(PID，PID对应内存使用大小，PID对应CPU使用率，PID对应内存使用率)
PID:14429,mem:189408,CPU percent:0.0% mem percent:10.1%
PID:20823,mem:292076,CPU percent:0.0% mem percent:15.6%
PID:21641,mem:382872,CPU percent:0.0% mem percent:20.4%
PID:27033,mem:347992,CPU percent:0.0% mem percent:18.5%
```

3）.找到对应CPU使用较高的PID

    top -H -p pid

```java
root@VM-4-5-ubuntu:/usr/local/rocketmq# top -H -p 14429
top - 09:18:50 up 2 days, 41 min,  1 user,  load average: 0.01, 0.01, 0.00
Threads:  37 total,   0 running,  37 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.7 us,  1.0 sy,  0.0 ni, 98.3 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  1876832 total,    80872 free,  1401604 used,   394356 buff/cache
KiB Swap:        0 total,        0 free,        0 used.   311844 avail Mem 

  PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND                                                                                                                                 
14429 root      20   0 2783488 189672   8920 S  0.0 10.1   0:00.00 java                                                                                                                                    
14430 root      20   0 2783488 189672   8920 S  0.0 10.1   0:01.38 java                                                                                                                                    
14431 root      20   0 2783488 189672   8920 S  0.0 10.1   0:01.69 java 
```

4）.将占用最高的 pid 转换为 16 进制 printf '%x\n' pid 得到 nid    

**（待测试）**     

    printf '%x\n' pid

```java
root@VM-4-5-ubuntu:/usr/local/rocketmq# printf '%x\n' 14429
385d
```

#### 1.2 jstack 分析 CPU 问题

**（待测试）**    

    使用nid查询堆栈消息
    jstack pid |grep 'nid' -C5 –color：
    或者 jstack pid

    也可以生成 jstack 文件进行分析。一般关注 WAITING 和 TIMED_WAITING 的部分，BLOCKED 就不用说了。
    然后对nid出现的问题WAITING与TIMED_WAITING部分进行grep -i '问题'；查看如果WAITING 之类的特别多，那么多半是有问题。

```java
root@VM-4-5-ubuntu:/usr/local/rocketmq# jstack 21641
        "ClientManageThread_12" #96 prio=5 os_prio=0 tid=0x00007f190003f770 nid=0x55fa waiting on condition [0x00007f18c54c2000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f00e6fd0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "ClientManageThread_11" #95 prio=5 os_prio=0 tid=0x00007f18dc00ae70 nid=0x55f0 waiting on condition [0x00007f18c55c3000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f00e6fd0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "ClientManageThread_10" #94 prio=5 os_prio=0 tid=0x00007f18dc009ae0 nid=0x55ef waiting on condition [0x00007f18c56c4000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f00e6fd0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_16" #93 prio=5 os_prio=0 tid=0x00007f190003e400 nid=0x55e1 waiting on condition [0x00007f18c57c5000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_15" #92 prio=5 os_prio=0 tid=0x00007f190003cca0 nid=0x55d9 waiting on condition [0x00007f18c58c6000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_14" #91 prio=5 os_prio=0 tid=0x00007f190003b930 nid=0x55d8 waiting on condition [0x00007f18c59c7000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_13" #90 prio=5 os_prio=0 tid=0x00007f190003a5c0 nid=0x558e waiting on condition [0x00007f18c5ac8000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_12" #89 prio=5 os_prio=0 tid=0x00007f19000393f0 nid=0x5554 waiting on condition [0x00007f18c5bc9000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_11" #88 prio=5 os_prio=0 tid=0x00007f1900038240 nid=0x554d waiting on condition [0x00007f18c5cca000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "NettyServerCodecThread_1" #87 prio=5 os_prio=0 tid=0x00007f19040188e0 nid=0x554c waiting on condition [0x00007f18c5dcb000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0cfffd8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at io.netty.util.concurrent.SingleThreadEventExecutor.takeTask(SingleThreadEventExecutor.java:243)
        at io.netty.util.concurrent.DefaultEventExecutor.run(DefaultEventExecutor.java:64)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at java.lang.Thread.run(Thread.java:748)

        "NettyServerNIOSelector_3_1" #86 prio=5 os_prio=0 tid=0x00007f190801d590 nid=0x554b runnable [0x00007f18c5ecc000]
        java.lang.Thread.State: RUNNABLE
        at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
        at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
        at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
        at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
        - locked <0x00000000f0bb1bf8> (a io.netty.channel.nio.SelectedSelectionKeySet)
        - locked <0x00000000f0bb2cf8> (a java.util.Collections$UnmodifiableSet)
        - locked <0x00000000f0bb2c20> (a sun.nio.ch.EPollSelectorImpl)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:101)
        at io.netty.channel.nio.SelectedSelectionKeySetSelector.select(SelectedSelectionKeySetSelector.java:68)
        at io.netty.channel.nio.NioEventLoop.select(NioEventLoop.java:810)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:457)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_10" #85 prio=5 os_prio=0 tid=0x00007f18f400b540 nid=0x6fc5 waiting on condition [0x00007f18c5fcd000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_9" #84 prio=5 os_prio=0 tid=0x00007f18d80179a0 nid=0x6fa8 waiting on condition [0x00007f18c60ce000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_8" #83 prio=5 os_prio=0 tid=0x00007f18dc006e50 nid=0x6280 waiting on condition [0x00007f18c61cf000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "AdminBrokerThread_7" #82 prio=5 os_prio=0 tid=0x00007f18d4430030 nid=0x626c waiting on condition [0x00007f18c62d0000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f0b61808> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

        "ClientManageThread_9" #81 prio=5 os_prio=0 tid=0x00007f1900036a00 nid=0x60bd waiting on condition [0x00007f18c63d1000]
        java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000f00e6fd0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

```
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