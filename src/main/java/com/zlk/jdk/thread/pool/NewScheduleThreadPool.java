package com.zlk.jdk.thread.pool;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 创建一个定长的线程池，而且支持定时的以及周期性的任务执行，支持定时及周期性任务执行。（定时调度的线程池）
 * public ScheduledThreadPoolExecutor(int corePoolSize) {
 *         super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
 *               new DelayedWorkQueue());
 * }
 * public ThreadPoolExecutor(int corePoolSize,
 *                               int maximumPoolSize,
 *                               long keepAliveTime,
 *                               TimeUnit unit,
 *                               BlockingQueue<Runnable> workQueue) {
 *         this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
 *              Executors.defaultThreadFactory(), defaultHandler);
 *  }
 * @author likuan.zhou
 * @date 2022/1/12/012 8:51
 */
public class NewScheduleThreadPool {
    final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @SneakyThrows
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        System.out.println("提交时间: " + sdf.format(new Date()));
        //延迟3秒钟后执行任务。默认不会自己结束线程
      /*  executor.schedule(new Runnable() {
            @Override
            public void run() {
               System.out.println("运行时间: " + sdf.format(new Date())+";threadId:"+Thread.currentThread().getId());
            }
        }, 3, TimeUnit.SECONDS);*/

        //延迟1秒钟后每隔3秒执行一次任务。默认不会自己结束线程
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("运行时间: " + sdf.format(new Date())+";threadId:"+Thread.currentThread().getId());
            }
        }, 1, 3, TimeUnit.SECONDS);
        /** 延迟1秒钟后每隔3秒执行一次任务
         提交时间: 09:01:23
         运行时间: 09:01:24;threadId:14
         运行时间: 09:01:27;threadId:14
         运行时间: 09:01:30;threadId:16
         运行时间: 09:01:33;threadId:16
         运行时间: 09:01:36;threadId:16
         运行时间: 09:01:39;threadId:16
         运行时间: 09:01:42;threadId:16
         ......
         */
        //Thread.sleep(10000);
        //executor.shutdown();
    }
}
