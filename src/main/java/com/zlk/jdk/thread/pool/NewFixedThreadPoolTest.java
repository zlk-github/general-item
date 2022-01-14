package com.zlk.jdk.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。（固定数量的线程池）
 *     FixedThreadPool是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。
 *
 * public static ExecutorService newFixedThreadPool(int nThreads) {
 *         return new ThreadPoolExecutor(nThreads, nThreads,
 *                                       0L, TimeUnit.MILLISECONDS,
 *                                       new LinkedBlockingQueue<Runnable>());
 * }
 *
 * @author likuan.zhou
 * @date 2022/1/11/011 9:07
 */
public class NewFixedThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        // 预期创建了5个线程，交替执行后只有5个线程id。执行完任务后该工作线程不终止。
        for (int i = 0; i < 10; i++) {
            executor.submit(()->{
                try {
                    Thread.sleep(1000L);
                    System.out.println("threadId:"+Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        /** 以上任务,创建了5个线程，交替执行后只有5个线程id。执行完任务后该工作线程不终止。
         threadId:15
         threadId:18
         threadId:14
         threadId:16
         threadId:17
         threadId:17
         threadId:15
         threadId:16
         threadId:14
         threadId:18
         */
    }
}
