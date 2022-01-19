package com.zlk.jdk.thread.general;

import lombok.SneakyThrows;

/**
 * 优先执行队列
 *
 * @author likuan.zhou
 * @date 2022/1/19/019 9:09
 */
public class PriorityThreadRunnableTask implements Runnable,Comparable<PriorityThreadRunnableTask>{
    private int priority;

    /**需要入参可以提供构造方法*/
    public PriorityThreadRunnableTask(int priority) {
        this.priority = priority;
    }

    public PriorityThreadRunnableTask() {
    }


    @SneakyThrows
    @Override
    public void run() {
        //休眠1秒，调用sleep()方法的过程中，线程不会释放对象锁
        System.out.println("线程id："+Thread.currentThread().getId()+"; 线程名称："+Thread.currentThread().getName()+"; priority:"+this.priority);
    }

    //当前对象和其他对象做比较，当前优先级大就返回-1，优先级小就返回1,值越小优先级越高
    @Override
    public int compareTo(PriorityThreadRunnableTask o) {
        return this.priority > o.priority ? -1 : 1;
    }
}
