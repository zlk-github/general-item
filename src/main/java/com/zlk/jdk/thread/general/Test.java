package com.zlk.jdk.thread.general;

/**
 * @author likuan.zhou
 * @title: Test
 * @projectName general-item
 * @description: TODO
 * @date 2021/9/6/006 9:12
 */
public class Test {
    public static void main(String[] args) {
        //测试线程,需要入参使用MyThread创建属性与构造方法
        MyThread myThread = new MyThread();
        myThread.start();
        MyThread myThread1 = new MyThread();
        myThread1.start();
        System.out.println("需要入参使用MyThread创建属性与构造方法");
    }
}
