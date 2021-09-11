package com.zlk.jdk.thread.general;

import lombok.SneakyThrows;

/**
 * @author likuan.zhou
 * @title: MyThreadRunnable
 * @projectName general-item
 * @description: 实现Runnable实现多线程,重写run()方法
 * @date 2021/9/8/008 19:45
 */
public class MyThreadRunnable implements Runnable {
    @SneakyThrows
    @Override
    public void run() {
        //休眠1秒，调用sleep()方法的过程中，线程不会释放对象锁
        Thread.sleep(1000);
        System.out.println("线程id："+Thread.currentThread().getId()+";线程名称："+Thread.currentThread().getName());
    }
}
