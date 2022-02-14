package com.zlk.jdk.thread.pool;

import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2022/1/12/012 9:14
 */
public class ThreadPoolExecutorShutdownTest {
    // public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE), new ThreadPoolExecutor.DiscardOldestPolicy());

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
        // shutDown();
        // shutdownNow();
        awaitTermination();
    }

    /**
     *
     * 使用shutdown正常关闭（正常选用）
     *
     *     将线程池状态置为SHUTDOWN,线程池并不会立即停止：
     *     停止接收外部submit的任务
     *     内部正在跑的任务和队列里等待的任务，会执行完
     *     等到第二步完成后，才真正停止
     */
    public static void shutDown() {
        // 核心线程3，最大线程池6。队列2
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 6, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2), new ThreadPoolExecutor.AbortPolicy());

        //从输出结果看到,输出了5个线程的打印,其中3个是线程池里面的,有2个是在队列里面的,也就是说正在运行的和队列里面的任务全部可以执行完成。在shutDown()之后提交,会报错,执行拒绝默认的策略.
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("before shutdown"+Thread.currentThread().getName());
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("after shutdown"+Thread.currentThread().getName());
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        /** 输出结果：
         before shutdownpool-1-thread-3
         before shutdownpool-1-thread-1
         before shutdownpool-1-thread-2
         Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task com.zlk.jdk.thread.pool.ThreadPoolExecutorShutdownTest$2@3a4afd8d rejected from java.util.concurrent.ThreadPoolExecutor@1996cd68[Shutting down, pool size = 3, active threads = 3, queued tasks = 2, completed tasks = 0]
         at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
         at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
         at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorShutdownTest.shutDown(ThreadPoolExecutorShutdownTest.java:62)
         at com.zlk.jdk.thread.pool.ThreadPoolExecutorShutdownTest.main(ThreadPoolExecutorShutdownTest.java:30)
         before shutdownpool-1-thread-2
         before shutdownpool-1-thread-3
         */
    }

    /**
     *
     * 使用shutdownNow强行关闭
     *
     *     将线程池状态置为STOP。企图立即停止，事实上不一定：
     *     跟shutdown()一样，先停止接收外部提交的任务
     *     忽略队列里等待的任务
     *     尝试将正在跑的任务interrupt中断
     *     返回未执行的任务列表
     */
    public static void shutdownNow() {
        // 核心线程3，最大线程池6。队列2
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 6, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2));

        //从输出结果看到,输出了5个线程的打印,其中3个是线程池里面的,有2个是在队列里面的,也就是说正在运行的和队列里面的任务全部可以执行完成。在shutDown()之后提交,会报错,执行拒绝默认的策略.
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        List<Runnable> runnableList = threadPoolExecutor.shutdownNow();
        System.out.println(runnableList);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("after shutdownNow"+Thread.currentThread().getName());
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        /** 输出结果：

         */
    }

    /**
     *
     * awaitTermination()方法的作用:
     *
     * 当前线程阻塞，直到
     *
     * 等所有已提交的任务（包括正在跑的和队列中等待的）执行完
     * 或者等超时时间到
     * 或者线程被中断，抛出InterruptedException
     * 然后返回true（shutdown请求后所有任务执行完毕）或false（已超时）
     */
    @SneakyThrows
    public static void awaitTermination() {
        // 核心线程3，最大线程池6。队列2
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 6, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2));

        //awaitTermination()是阻塞的，返回结果是线程池是否已停止（已经停止就是true,此含义包括线程池已经shutdown并且没有还在运行的线程）,awaitTermination并不是用来关闭线程池，它只是用来检测timeout时间后线程池是否关闭。线程池关闭之后就会返回true,一般在调用shutdown()方法后调用
        //————————————————
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        boolean a = threadPoolExecutor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("======="+a);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("after threadPoolExecutor:"+Thread.currentThread().getName());
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        threadPoolExecutor.shutdown();
        boolean b = threadPoolExecutor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("+++++++"+b);
        /** 输出结果：
         pool-1-thread-1
         pool-1-thread-3
         pool-1-thread-2
         pool-1-thread-3
         pool-1-thread-2
         =======false
         after threadPoolExecutor:pool-1-thread-1
         +++++++true
         */
    }


}
