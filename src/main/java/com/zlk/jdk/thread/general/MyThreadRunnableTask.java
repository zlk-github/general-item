package com.zlk.jdk.thread.general;

import lombok.SneakyThrows;

/**
 * @author likuan.zhou
 * @title: MyThreadRunnable
 * @projectName general-item
 * @description: 实现Runnable实现多线程,重写run()方法
 * @date 2021/9/8/008 19:45
 */
public class MyThreadRunnableTask<T> implements Runnable {
    private T t;

    /**需要入参可以提供构造方法*/
    public MyThreadRunnableTask(T t) {
        this.t = t;
    }

    public MyThreadRunnableTask() {
    }

    @SneakyThrows
    @Override
    public void run() {
        //休眠1秒，调用sleep()方法的过程中，线程不会释放对象锁
        System.out.println("This is ThreadPoolExetor#submit(Runnable task, T result) method.");
    }
}
