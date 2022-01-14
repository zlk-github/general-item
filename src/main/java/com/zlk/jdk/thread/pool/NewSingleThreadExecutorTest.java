package com.zlk.jdk.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 创建一个单线程化的Executor，即只创建唯一的工作者线程来执行任务，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。（单一线程的线程池）
 * 如果这个线程异常结束，会有另一个取代它，保证顺序执行。单工作线程最大的特点是可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。
 *
 *  public static ExecutorService newSingleThreadExecutor() {
 *         return new FinalizableDelegatedExecutorService
 *             (new ThreadPoolExecutor(1, 1,
 *                                     0L, TimeUnit.MILLISECONDS,
 *                                     new LinkedBlockingQueue<Runnable>()));
 * }
 *
 * @author likuan.zhou
 * @date 2022/1/11/011 9:12
 */
public class NewSingleThreadExecutorTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // 预期创建了1个线程，任务执行后只有1个线程id。执行完任务后该工作线程不终止（如果这个线程异常结束，会有另一个取代它，保证顺序执行。）。
        for (int i = 0; i < 5; i++) {
            executor.submit(()->{
                try {
                    Thread.sleep(1000L);
                    System.out.println("threadId:"+Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.submit(()->{
                try {
                    if (finalI == 0) {
                        throw new Exception();
                    }
                    Thread.sleep(1000L);
                    System.out.println("threadId:"+Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        /**预期创建了1个线程，任务执行后只有1个线程id。执行完任务后该工作线程不终止（如果这个线程异常结束，会有另一个取代它，保证顺序执行。）。
         threadId:14
         threadId:14
         threadId:14
         threadId:14
         threadId:14
         java.lang.Exception
         at com.zlk.jdk.thread.pool.NewSingleThreadExecutorTest.lambda$main$1(NewSingleThreadExecutorTest.java:34)
         at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
         at java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:266)
         at java.util.concurrent.FutureTask.run(FutureTask.java)
         at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         at java.lang.Thread.run(Thread.java:748)
         threadId:14
         threadId:14
         threadId:14
         threadId:14
         */
    }
}
