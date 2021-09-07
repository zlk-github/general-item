package com.zlk.jdk.thread.general;

import lombok.SneakyThrows;

/**
 * @author likuan.zhou
 * @title: MyThread
 * @projectName general-item
 * @description: TODO
 * @date 2021/9/7/007 8:55
 */
public class MyThread extends Thread{

    /**重写run方法*/
    @SneakyThrows
    @Override
    public void run(){
        //休眠1秒，调用sleep()方法的过程中，线程不会释放对象锁
        Thread.sleep(1000);
        System.out.println("线程id："+Thread.currentThread().getId()+";线程名称："+Thread.currentThread().getName());
    }
}
