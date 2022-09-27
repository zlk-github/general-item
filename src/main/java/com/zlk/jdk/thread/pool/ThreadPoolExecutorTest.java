package com.zlk.jdk.thread.pool;

import com.zlk.jdk.thread.general.MyThreadRunnableTask;
import com.zlk.jdk.thread.general.PriorityThreadRunnableTask;
import com.zlk.domain.po.User;

import java.util.concurrent.*;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2022/1/12/012 9:14
 */
public class ThreadPoolExecutorTest {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE), new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     public ThreadPoolExecutor(int corePoolSize, // 核心线程数
                                 int maximumPoolSize, // 最大线程数
                                 long keepAliveTime,  //当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
                                 TimeUnit unit, //单位
                                 BlockingQueue<Runnable> workQueue, //任务队列，被提交但尚未被执行的任务。(分为直接提交队列、有界任务队列、无界任务队列、优先任务队列；)
                                 ThreadFactory threadFactory, //线程工厂，用于创建线程，一般用默认的即可。
                                 RejectedExecutionHandler handler //拒绝策略，当任务太多来不及处理，如何拒绝任务。(AbortPolicy策略,CallerRunsPolicy策略,DiscardOledestPolicy策略,DiscardPolicy策略)
                            ) {
     */


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // execute方法用于提交不需要返回值的任务，利用这种方式提交的任务无法得知是否正常执行
        // execute();
        // submit方法执行，可以有返回值  && 自定义线程名称
        submit();

        // 1直接提交队列,会阻塞，最多执行任务的线程数量小于maximumPoolSize。否则很容易触发拒绝策略
        // testSynchronousQueue();
        // 2有界的任务队列,
        // testArrayBlockingQueue();
        // 3无界队列
        // testLinkedBlockingQueue();
        // 4优先任务队列
        // testPriorityBlockingQueue();

        // 拒绝策略
        // testRejectionPoliciesAbortPolicy();
        // testRejectionPoliciesCallerRunsPolicy();
        // testRejectionPoliciesDiscardOldestPolicy();
        //testRejectionPoliciesDiscardPolicy();

    }

    /**
     * execute方法用于提交不需要返回值的任务，利用这种方式提交的任务无法得知是否正常执行
     */
    public static void execute() {
        //1 execute方法用于提交不需要返回值的任务，利用这种方式提交的任务无法得知是否正常执行
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("This is ThreadPoolExetor#execute method.");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
      * submit方法执行，可以有返回值 && 自定义线程名称MyThreadRunnableTask
     */
    public static void submit() throws ExecutionException, InterruptedException {
        // 2 submit
        // 2.1 submit(Callable<T> task) 有返回
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("This is ThreadPoolExetor#submit(Callable<T> task) method.");
                return "result";
            }
        };
        Future<String> future = threadPoolExecutor.submit(callable);
        System.out.println("future-->"+future.get());

        // 2.2 submit(Runnable task, T result) 有返回 && 自定义线程名称MyThreadRunnableTask
        User user = new User();
        user.setName("cs1001");
        Future<User> future2 = threadPoolExecutor.submit(new MyThreadRunnableTask<User>(user), user);
        System.out.println("future2-->"+future2.get());

        // 2.3 submit(Runnable task)  无返回
       Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("This is ThreadPoolExetor#submit(Runnable runnable) method.");
            }
        };
        Future<?> future3 = threadPoolExecutor.submit(runnable);
        System.out.println("future3-->"+future3.get());

        /** 允许结果
         Connected to the target VM, address: '127.0.0.1:14526', transport: 'socket'
         This is ThreadPoolExetor#submit(Callable<T> task) method.
         future-->result
         This is ThreadPoolExetor#submit(Runnable task, T result) method.;线程id：15;线程名称：custompool-1-thread-2
         future2-->User(id=null, name=cs1001, sex=null)
         This is ThreadPoolExetor#submit(Runnable runnable) method.
         future3-->null
         */
    }

    /**
     * 1. 直接提交队列SynchronousQueue
     *     SynchronousQueue是一个特殊的BlockingQueue，它没有容量，每执行一个插入操作就会阻塞，需要再执行一个删除操作才会被唤醒，反之每一个删除操作也都要等待对应的插入操作。
     *     当任务队列为SynchronousQueue，创建的线程数大于maximumPoolSize时，会直接执行拒绝策略抛出异常。
     *     使用SynchronousQueue队列，提交的任务不会被保存，总是会马上提交执行。如果用于执行任务的线程数量小于maximumPoolSize，则尝试创建新的进程，
     *     如果达到maximumPoolSize设置的最大值，则根据你设置的handler执行拒绝策略。因此这种方式你提交的任务不会被缓存起来，而是会被马上执行，在这种情况下，你需要对你程序的并发量有个准确的评估，才能设置合适的maximumPoolSize数量，否则很容易就会执行拒绝策略；
     */
    public static void testSynchronousQueue() {
        //1 execute方法用于提交不需要返回值的任务，利用这种方式提交的任务无法得知是否正常执行
        //maximumPoolSize设置为2 ，拒绝策略为AbortPolic策略，直接抛出异常
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i<3; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        /** 预期结果maximumPoolSize设置为2 ，拒绝策略为AbortPolic策略，直接抛出异常
         pool-2-thread-2
         pool-2-thread-1
         Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task com.zlk.jdk.thread.pool.ThreadPoolExecutorTest$4@2db0f6b2 rejected from java.util.concurrent.ThreadPoolExecutor@3339ad8e[Running, pool size = 2, active threads = 1, queued tasks = 0, completed tasks = 1]
         at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
         at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
         at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorTest.testSynchronousQueue(ThreadPoolExecutorTest.java:112)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorTest.main(ThreadPoolExecutorTest.java:45)
         */
    }

    /**
     * 2. 有界的任务队列ArrayBlockingQueue
     *
     *     使用ArrayBlockingQueue有界任务队列，若有新的任务需要执行时，线程池会创建新的线程，直到创建的线程数量达到corePoolSize时，则会将新的任务加入到等待队列中。若等待队列已满，即超过ArrayBlockingQueue初始化的容量，则继续创建线程，直到线程数量达到maximumPoolSize设置的最大线程数量，
     *     若大于maximumPoolSize，则执行拒绝策略。在这种情况下，线程数量的上限与有界任务队列的状态有直接关系，如果有界队列初始容量较大或者没有达到超负荷的状态，线程数将一直维持在corePoolSize以下，反之当任务队列已满时，则会以maximumPoolSize为最大线程数上限。
     */
    public static void testArrayBlockingQueue() {
        // 2 execute方法用于提交不需要返回值的任务，利用这种方式提交的任务无法得知是否正常执行
        // maximumPoolSize设置为5 ，拒绝策略为AbortPolic策略，若大于maximumPoolSize直接抛出异常
        // 实际使用中可以设置ArrayBlockingQueue初始大小为Integer.MAX_VALUE做排队。但是这样的话maximumPoolSize短时间会很难生效,导致大部分时间都只有主线程。
       /* ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 5, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i<100; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }*/
        /** 预期结果maximumPoolSize设置为5 ，拒绝策略为AbortPolic策略，若大于maximumPoolSize直接抛出异常
         pool-2-thread-1
         pool-2-thread-2
         pool-2-thread-3
         pool-2-thread-2
         pool-2-thread-1
         pool-2-thread-2
         pool-2-thread-5
         pool-2-thread-3
         pool-2-thread-4
         pool-2-thread-3
         pool-2-thread-5
         pool-2-thread-2
         pool-2-thread-1
         pool-2-thread-2
         pool-2-thread-5
         pool-2-thread-3
         pool-2-thread-4
         Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task com.zlk.jdk.thread.pool.ThreadPoolExecutorTest$5@2db0f6b2 rejected from java.util.concurrent.ThreadPoolExecutor@3339ad8e[Running, pool size = 5, active threads = 5, queued tasks = 3, completed tasks = 8]
         at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
         at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
         at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorTest.testArrayBlockingQueue(ThreadPoolExecutorTest.java:144)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorTest.main(ThreadPoolExecutorTest.java:47)
         */
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(1, 5, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1000),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i<100; i++) {
            pool2.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        /** 队列过大，只有主线程
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         .......
         pool-2-thread-1
         */
    }

    /**
     * 3. 无界的任务队列LinkedBlockingQueue
     *
     *     使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，也就是说在这种情况下maximumPoolSize这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize后，就不会再增加了(原则上都在队列里面等待)；
     *     若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
     */
    public static void testLinkedBlockingQueue() {
        // 3. 无界的任务队列LinkedBlockingQueue
        //   使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，也就是说在这种情况下maximumPoolSize这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize后，就不会再增加了；
        //   若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
        // LinkedBlockingQueue默认大小Integer.MAX_VALUE
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 5, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i<100; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        /** 预期结果maximumPoolSize设置为5 ，拒绝策略为AbortPolic策略，若大于maximumPoolSize直接抛出异常
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-1
         ........
         pool-2-thread-1
         */
    }

    /**
     * 4. 优先任务队列PriorityBlockingQueue
     *
     *     PriorityBlockingQueue它其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize的数量，只不过其他队列一般是按照先进先出的规则处理任务，
     *     而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
     */
    public static void testPriorityBlockingQueue() {
        // 4. 优先任务队列PriorityBlockingQueue
        //PriorityBlockingQueue它其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize的数量，只不过其他队列一般是按照先进先出的规则处理任务，
        //而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 5, 1000, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i<100; i++) {
            pool.execute(new PriorityThreadRunnableTask(i));
        }
        /** 预期结果maximumPoolSize设置为5 ，但是只有一个线程，执行结果有序（最开始几个不能保证顺序），拒绝策略为AbortPolic策略，
         线程id：14; 线程名称：pool-2-thread-1; priority:0
         线程id：14; 线程名称：pool-2-thread-1; priority:75
         线程id：14; 线程名称：pool-2-thread-1; priority:86
         线程id：14; 线程名称：pool-2-thread-1; priority:99
         线程id：14; 线程名称：pool-2-thread-1; priority:98
         线程id：14; 线程名称：pool-2-thread-1; priority:97
         ........
         线程id：14; 线程名称：pool-2-thread-1; priority:2
         线程id：14; 线程名称：pool-2-thread-1; priority:1
         */
    }


    /**
     * 拒绝策略AbortPolicy
     *
     *线程池中的线程已经用完了，无法继续为新任务服务，同时，等待队列也已经排满了，再也塞不下新任务了。这时候我们就需要拒绝策略机制合理的处理这个问题。
     *JDK 内置的拒绝策略如下：
     *  1. AbortPolicy ： 直接抛出异常，阻止系统正常运行。
     *  2. CallerRunsPolicy ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。
     *  3. DiscardOldestPolicy ： 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务。
     *  4. DiscardPolicy ： 该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。
     *  以上内置拒绝策略均实现了 RejectedExecutionHandler 接口，若以上策略仍无法满足实际需要，完全可以自己扩展 RejectedExecutionHandler 接口
     */
    public static void testRejectionPoliciesAbortPolicy() {
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(1, 3, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i=0; i<20; i++) {
            pool2.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        System.out.println("------该内容不能输出--------");
        /** AbortPolicy ： 直接抛出异常，阻止系统正常运行。
         pool-2-thread-2
         pool-2-thread-3
         pool-2-thread-1
         pool-2-thread-3
         pool-2-thread-2
         pool-2-thread-1
         Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task com.zlk.jdk.thread.pool.ThreadPoolExecutorTest$7@2db0f6b2 rejected from java.util.concurrent.ThreadPoolExecutor@3339ad8e[Running, pool size = 3, active threads = 0, queued tasks = 0, completed tasks = 6]
         at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
         at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
         at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorTest.testRejectionPoliciesAbortPolicy(ThreadPoolExecutorTest.java:279)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorTest.main(ThreadPoolExecutorTest.java:55)
         */
    }

    /**
     * 拒绝策略CallerRunsPolicy
     *
     *线程池中的线程已经用完了，无法继续为新任务服务，同时，等待队列也已经排满了，再也塞不下新任务了。这时候我们就需要拒绝策略机制合理的处理这个问题。
     *JDK 内置的拒绝策略如下：
     *  1. AbortPolicy ： 直接抛出异常，阻止系统正常运行。
     *  2. CallerRunsPolicy ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。 ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。
     *  3. DiscardOldestPolicy ： 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务。
     *  4. DiscardPolicy ： 该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。
     *  以上内置拒绝策略均实现了 RejectedExecutionHandler 接口，若以上策略仍无法满足实际需要，完全可以自己扩展 RejectedExecutionHandler 接口
     */
    public static void testRejectionPoliciesCallerRunsPolicy() {
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(1, 3, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),Executors.defaultThreadFactory(),new ThreadPoolExecutor.CallerRunsPolicy());
        for(int i=0; i<20; i++) {
            pool2.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        System.out.println("------该内容能输出--------");
        /** CallerRunsPolicy ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。main为其他线程
         pool-2-thread-1
         pool-2-thread-3
         pool-2-thread-2
         main
         pool-2-thread-2
         pool-2-thread-3
         pool-2-thread-1
         pool-2-thread-3
         pool-2-thread-2
         main
         pool-2-thread-1
         pool-2-thread-1
         pool-2-thread-3
         main
         pool-2-thread-1
         pool-2-thread-2
         pool-2-thread-1
         ------该内容能输出--------
         pool-2-thread-3
         pool-2-thread-1
         pool-2-thread-2
         */
    }

    /**
     * 拒绝策略DiscardOldestPolicy
     *
     *线程池中的线程已经用完了，无法继续为新任务服务，同时，等待队列也已经排满了，再也塞不下新任务了。这时候我们就需要拒绝策略机制合理的处理这个问题。
     *JDK 内置的拒绝策略如下：
     *  1. AbortPolicy ： 直接抛出异常，阻止系统正常运行。
     *  2. CallerRunsPolicy ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。 ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。
     *  3. DiscardOldestPolicy ： 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务。
     *  4. DiscardPolicy ： 该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。
     *  以上内置拒绝策略均实现了 RejectedExecutionHandler 接口，若以上策略仍无法满足实际需要，完全可以自己扩展 RejectedExecutionHandler 接口
     */
    public static void testRejectionPoliciesDiscardOldestPolicy() {
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(1, 3, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardOldestPolicy());
        for(int i=0; i<20; i++) {
            pool2.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        System.out.println("------该内容能输出--------");
        /** DiscardOldestPolicy ： 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务。(任务出现了丢弃)
         pool-2-thread-1
         pool-2-thread-2
         pool-2-thread-1
         pool-2-thread-2
         ------该内容能输出--------
         pool-2-thread-1
         pool-2-thread-2
         pool-2-thread-2
         pool-2-thread-3
         pool-2-thread-1
         */
    }

    /**
     * 拒绝策略DiscardPolicy
     *
     *线程池中的线程已经用完了，无法继续为新任务服务，同时，等待队列也已经排满了，再也塞不下新任务了。这时候我们就需要拒绝策略机制合理的处理这个问题。
     *JDK 内置的拒绝策略如下：
     *  1. AbortPolicy ： 直接抛出异常，阻止系统正常运行。
     *  2. CallerRunsPolicy ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。 ： 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能会急剧下降。
     *  3. DiscardOldestPolicy ： 丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务。
     *  4. DiscardPolicy ： 该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。
     *  以上内置拒绝策略均实现了 RejectedExecutionHandler 接口，若以上策略仍无法满足实际需要，完全可以自己扩展 RejectedExecutionHandler 接口
     */
    public static void testRejectionPoliciesDiscardPolicy() {
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(1, 3, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardPolicy());
        for(int i=0; i<20; i++) {
            pool2.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        System.out.println("------该内容能输出--------");
        /** DiscardPolicy ： 该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。(任务出现了丢弃)
         pool-2-thread-1
         ------该内容能输出--------
         pool-2-thread-3
         pool-2-thread-2
         pool-2-thread-3
         pool-2-thread-1
         pool-2-thread-2
         */
    }
}
