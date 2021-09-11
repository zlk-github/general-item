package com.zlk.jdk.thread.general;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author likuan.zhou
 * @title: Test
 * @projectName general-item
 * @description: TODO
 * @date 2021/9/6/006 9:12
 */
public class Test {
    private static final String THREAD_GROUP = "线程组1";
    private static final String THREAD_NAME_ONE = "线程1";
    private static final String THREAD_NAME_TWO = "线程2";

    public static void main(String[] args) {
        testMyThreadCallable();
    }

    public static void testMyThread() {
        //测试线程,需要入参使用MyThread创建属性与构造方法
        MyThread myThread = new MyThread();
        //start方法调用后，线程进入就绪状态等待分配，当分配到cpu后进入运行状态。
        myThread.start();
        MyThread myThread1 = new MyThread();
        myThread1.start();
        System.out.println("需要入参使用MyThread创建属性与构造方法");
    }

    public static void testMyThreadRunnable() {
        MyThreadRunnable myThreadRunnable = new MyThreadRunnable();
        //Thread thread1 = new Thread(new ThreadGroup(THREAD_GROUP),myThreadRunnable1,THREAD_NAME_ONE);
        Thread thread1 = new Thread(myThreadRunnable,THREAD_NAME_ONE);
        thread1.start();

        Thread thread2 = new Thread(myThreadRunnable,THREAD_NAME_TWO);
        thread2.start();
    }

    public static void testMyThreadCallable() {
        List<FutureTask<String>> tasks = new ArrayList<FutureTask<String>>();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 2; i++) {
            //需要使用可以传对象
            MyThreadCallable<String> myThreadCallable = new MyThreadCallable<>();
            FutureTask<String> future = new FutureTask<String>(myThreadCallable);
            executorService.submit(future);
            tasks.add(future);
        }
        for (int i = 0; i < 2; i++) {
            try {
                String str = tasks.get(i).get();
                System.out.println(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }


}
