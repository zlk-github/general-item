## java多线程

### 1.普通线程创建与线程常见知识。

### 1.普通线程创建与线程常见知识。（com.zlk.jdk.thread.general包）

#### 1.1 什么是线程与进程，作用，使用场景

###### 1.1.1 线程介绍

    进程：每个进程都有独立的代码和数据空间（进程上下文），进程间的切换会有较大的开销，一个进程包含1--n个线程。（进程是资源分配的最小单位）
    线程：同一类线程共享代码和数据空间，每个线程有独立的运行栈和程序计数器(PC)，线程切换开销小。（线程是cpu调度的最小单位）

###### 1.1.2 线程优势

    提高并发，充分利用空闲CPU资源。

###### 1.1.3 线程劣势

    1.使用大量的线程，频繁切换线程会消耗资源影响性能。线程需要内存空间；
    2.使用不当会带来隐藏bug(如数据一致性，顺序等问题)；
    3.线程终止需要考虑对其他程序影响；
    4.多线程共享与调用需要考虑数据一致性（锁），以及死锁等问题。

###### 1.1.4 使用场景

    对一些耗时较多的任务可以选择使用多线程，如批量任务，文件读写，网络收发等。

#### 1.2 线程的状态与什么时候达成

![Alt text](../images/threadstate1001.png)

线程5个状态：新建、就绪、运行、阻塞、死亡。（待完善）

    1.初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
    2.运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。（就绪状态与运行中）
    线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，
    获取CPU的使用权， 此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
    3.阻塞(BLOCKED)：表示线程阻塞于锁。--（加锁）
    4.等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。--（锁释放）
    5.超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。
    6.终止(TERMINATED)：表示该线程已经执行完毕。


#### 1.3 线程创建方式

    1.继承Thread类创建线程 -- MyThread类
    2.实现Runnable接口创建线程,重写run()方法。(需要配合Thread使用)  -- MyThreadRunnable类
    3.使用Callable和Future创建线程，重写call()方法，可以拿到线程返回值。(需要配合Thread或者线程池使用)  -- MyThreadCallable类
    4.使用线程池例如用Executor框架，最常用。  -- 详见2.线程池
        // ExecutorService executorService = Executors.newCachedThreadPool();
        // ExecutorService executorService = Executors.newFixedThreadPool(5);
        // ExecutorService executorService = Executors.newSingleThreadExecutor();
        // Executors.newScheduledThreadPool(int)
        自定义（推荐）： new ThreadPoolExecutor(intcorePoolSize, intmaximumPoolSize, longkeepAliveTime,TimeUnitunit,BlockingQueue<Runnable>workQueue)自定义创建


#### 1.4 线程常用方法介绍

#### 1.5 线程数的核算

### 2.线程池

https://mp.weixin.qq.com/s?__biz=MzA3ODIxNjYxNQ==&mid=2247493401&idx=2&sn=08dcf94d57a9ec9e69bb2fbfd4c57a3d&chksm=9f448bd1a83302c7710b3a0d331bf0e70e7ac96e1941933e7027677406c257fb72d7c5fabab5&scene=21#wechat_redirect

#### 2.1 引入线程池背景

程序中大量的线程创建和销毁会占用大量系统资源，且不方便统一管理导致滥用线程影响系统性能。
使用线程池将能对线程进行统一管理（系统线程数不是无止境的），且减少线程创建销毁等开销。
但是有利有弊，使用线程池需要提前预估线程池（系统需要，硬件的限制等等）。

#### 2.2 常见线程池

// ExecutorService executorService = Executors.newCachedThreadPool();

// ExecutorService executorService = Executors.newFixedThreadPool(5);

// ExecutorService executorService = Executors.newSingleThreadExecutor();

// Executors.newScheduledThreadPool(int)

自定义（推荐）： new ThreadPoolExecutor(intcorePoolSize, intmaximumPoolSize, longkeepAliveTime,TimeUnitunit,BlockingQueue<Runnable>workQueue)自定义创建

#### 2.3 ThreadPoolExecutor创建流程

	如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
	
	如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
	
	如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
	
	如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。

### 3.常见锁java  

      volatile
      单机锁LOCK，synchronized
      分布式锁：redis,zookpeer,mysql等实现
      乐观锁CAS等
      多线程atomicInteger与分段锁 （https://www.cnblogs.com/muzhongjiang/p/15142938.html）

### 4.线程的原子性问题，可见性问题，有序性问题


