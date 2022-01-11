package com.zlk.jdk.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * （带缓存的线程池）创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 *
 *     这种类型的线程池特点是：
 *     工作线程的创建数量几乎没有限制(其实也有限制的,数目为Interger. MAX_VALUE), 这样可灵活的往线程池中添加线程。
 *     如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间(默认为1分钟)，则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
 *     在使用CachedThreadPool时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。
 *
 *    public static ExecutorService newCachedThreadPool() {
 *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *                                       60L, TimeUnit.SECONDS,
 *                                       new SynchronousQueue<Runnable>());
 *     }
 * @author likuan.zhou
 * @date 2022/1/11/011 8:45
 */
public class NewCachedThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        // 预期创建了10个线程，执行完任务后完成后一分钟该工作线程才自动终止。
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
        /** 以上任务，创建了10个线程，执行完任务后完成后一分钟该工作线程才自动终止。
         threadId:22
         threadId:18
         threadId:15
         threadId:19
         threadId:20
         threadId:23
         threadId:16
         threadId:14
         threadId:17
         threadId:21
         */
    }
}
