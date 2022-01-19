## java线程池

### 1.线程池

https://mp.weixin.qq.com/s?__biz=MzA3ODIxNjYxNQ==&mid=2247493401&idx=2&sn=08dcf94d57a9ec9e69bb2fbfd4c57a3d&chksm=9f448bd1a83302c7710b3a0d331bf0e70e7ac96e1941933e7027677406c257fb72d7c5fabab5&scene=21#wechat_redirect

#### 1.1 引入线程池背景

    程序中大量的线程创建和销毁会占用大量系统资源，且不方便统一管理导致滥用线程影响系统性能。
    使用线程池将能对线程进行统一管理（系统线程数不是无止境的），且减少线程创建销毁等开销。
    但是有利有弊，使用线程池需要提前预估线程池（系统需要，硬件的限制等等）。

###### 1.1.1 线程优势

    提高并发，充分利用空闲CPU资源。

###### 1.1.2 线程劣势

    1.使用大量的线程，频繁切换线程会消耗资源影响性能。线程需要内存空间；
    2.使用不当会带来隐藏bug(如数据一致性，顺序，死锁，资源不足，并发错误，线程泄漏，请求过载等问题)；
    3.线程终止需要考虑对其他程序影响；
    4.多线程共享与调用需要考虑数据一致性（锁），以及死锁等问题。

#### 1.2 常见线程池

    // ExecutorService executorService = Executors.newCachedThreadPool();
    
    // ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    // ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    // Executors.newScheduledThreadPool(int)
    
    自定义（推荐）： new ThreadPoolExecutor(intcorePoolSize, intmaximumPoolSize, longkeepAliveTime,TimeUnitunit,BlockingQueue<Runnable>workQueue)自定义创建

#### 1.3 ThreadPoolExecutor创建流程

	如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
	
	如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程（非核心线程）去执行这个任务；
	
	如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
	
	如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。

### 2 线程池使用

#### 2.1 newCachedThreadPool（带缓存的线程池）

创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。

    这种类型的线程池特点是：
    工作线程的创建数量几乎没有限制(其实也有限制的,数目为Interger. MAX_VALUE), 这样可灵活的往线程池中添加线程。
    如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间(默认为1分钟)，则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
    在使用CachedThreadPool时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。

#### 2.2 newFixedThreadPool（固定数量的线程池）

创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。

    FixedThreadPool是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。

#### 2.3 newSingleThreadExecutor（单一线程的线程池）

创建一个单线程化的Executor，即只创建唯一的工作者线程来执行任务，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
如果这个线程异常结束，会有另一个取代它，保证顺序执行。单工作线程最大的特点是可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。

#### 2.4 newScheduleThreadPool（定时调度的线程池）

创建一个定长的线程池，而且支持定时的以及周期性的任务执行，支持定时及周期性任务执行。

### 3 线程池的组成ThreadPoolExecutor

#### 3.1 一般的线程池主要分为以下 4 个组成部分：

    1. 线程池管理器：用于创建并管理线程池
    2. 工作线程：线程池中的线程
    3. 任务接口：每个任务必须实现的接口，用于工作线程调度其运行
    4. 任务队列：用于存放待处理的任务，提供一种缓冲机制

Java 中的线程池是通过 Executor 框架实现的，该框架中用到了 Executor，Executors，ExecutorService，ThreadPoolExecutor ，Callable 和 Future、FutureTask 这几个类。

ThreadPoolExecutor 的构造方法如下：

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), defaultHandler);
}
```


   1. corePoolSize：指定了线程池中的线程数量。
   2. maximumPoolSize：指定了线程池中的最大线程数量。
   3. keepAliveTime：当前线程池数量超过 corePoolSize 时，多余的空闲线程的存活时间，即多次时间内会被销毁。
   4. unit：keepAliveTime 的单位。
   5. workQueue：任务队列，被提交但尚未被执行的任务。
   6. threadFactory：线程工厂，用于创建线程，一般用默认的即可。
   7. handler：拒绝策略，当任务太多来不及处理，如何拒绝任务。

#### 3.2 workQueue任务队列

直接提交队列、有界任务队列、无界任务队列、优先任务队列；

1. 直接提交队列SynchronousQueue

    SynchronousQueue是一个特殊的BlockingQueue，它没有容量，每执行一个插入操作就会阻塞，需要再执行一个删除操作才会被唤醒，反之每一个删除操作也都要等待对应的插入操作。

    当任务队列为SynchronousQueue，创建的线程数大于maximumPoolSize时，会直接执行拒绝策略抛出异常。
    
    使用SynchronousQueue队列，提交的任务不会被保存，总是会马上提交执行。如果用于执行任务的线程数量小于maximumPoolSize，则尝试创建新的进程，
    如果达到maximumPoolSize设置的最大值，则根据你设置的handler执行拒绝策略。因此这种方式你提交的任务不会被缓存起来，而是会被马上执行，在这种情况下，你需要对你程序的并发量有个准确的评估，才能设置合适的maximumPoolSize数量，否则很容易就会执行拒绝策略；

2. 有界的任务队列ArrayBlockingQueue

    使用ArrayBlockingQueue有界任务队列，若有新的任务需要执行时，线程池会创建新的线程，直到创建的线程数量达到corePoolSize时，则会将新的任务加入到等待队列中。若等待队列已满，即超过ArrayBlockingQueue初始化的容量，则继续创建线程，直到线程数量达到maximumPoolSize设置的最大线程数量，
    若大于maximumPoolSize，则执行拒绝策略。在这种情况下，线程数量的上限与有界任务队列的状态有直接关系，如果有界队列初始容量较大或者没有达到超负荷的状态，线程数将一直维持在corePoolSize以下，反之当任务队列已满时，则会以maximumPoolSize为最大线程数上限。

3. 无界的任务队列LinkedBlockingQueue

    使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，也就是说在这种情况下maximumPoolSize这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize后，就不会再增加了；
    若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。

4. 优先任务队列PriorityBlockingQueue

    PriorityBlockingQueue它其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize的数量，只不过其他队列一般是按照先进先出的规则处理任务，
    而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。

#### 3.3 拒绝策略

线程池中的线程已经用完了，无法继续为新任务服务，同时，等待队列也已经排满了，再也塞不下新任务了。这时候我们就需要拒绝策略机制合理的处理这个问题。
JDK 内置的拒绝策略如下：

    1. AbortPolicy ： 直接抛出异常，阻止系统正常运行。
    2. CallerRunsPolicy ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。
    3. DiscardOldestPolicy ： 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务。
    4. DiscardPolicy ： 该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。
     以上内置拒绝策略均实现了 RejectedExecutionHandler 接口，若以上策略仍无法满足实际需要，完全可以自己扩展 RejectedExecutionHandler 接口

```java
public class ThreadPool {
    private static ExecutorService pool;
    public static void main( String[] args )
    {
        //自定义拒绝策略
        pool = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5),
                Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println(r.toString()+"执行了拒绝策略");
                
            }
        });
          
        for(int i=0;i<10;i++) {
            pool.execute(new ThreadTask());
        }    
    }
}

public class ThreadTask implements Runnable{    
    public void run() {
        try {
            //让线程阻塞，使后续任务进入缓存队列
            Thread.sleep(1000);
            System.out.println("ThreadName:"+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
    }
}
```
#### 3.4 ThreadPoolExecutor扩展

    ThreadPoolExecutor扩展主要是围绕beforeExecute()、afterExecute()和terminated()三个接口实现的，
    1、beforeExecute：线程池中任务运行前执行
    2、afterExecute：线程池中任务运行完毕后执行
    3、terminated：线程池退出后执行
    通过这三个接口我们可以监控每个任务的开始和结束时间，或者其他一些功能。

### 其他：线程的原子性问题，可见性问题，有序性问题

### 参考

    https://www.cnblogs.com/aaron911/p/6213808.html

    https://www.jianshu.com/p/7ab4ae9443b9

    https://www.cnblogs.com/dafanjoy/p/9729358.html

    https://www.jianshu.com/p/f030aa5d7a28
