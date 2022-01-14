package com.zlk.jdk.thread.pool;

import com.zlk.jdk.thread.general.MyThreadRunnableTask;
import com.zlk.po.User;

import java.util.concurrent.*;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2022/1/12/012 9:14
 */
public class ThreadPoolExecutorTest {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.DiscardOldestPolicy());

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

        execute();
        submit();
        /** 输出结果
         This is ThreadPoolExetor#execute method.
         This is ThreadPoolExetor#submit(Callable<T> task) method.
         future-->result
         This is ThreadPoolExetor#submit(Runnable task, T result) method.
         future2-->User(id=null, name=cs1001, sex=null)
         This is ThreadPoolExetor#submit(Runnable runnable) method.
         future3-->null
         */
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
      * submit方法执行，可以有返回值
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

        // 2.2 submit(Runnable task, T result) 有返回
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
    }
}
