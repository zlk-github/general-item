package com.zlk.jdk.thread.lock.reentrant;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likuan.zhou
 * @title: ReentrantTest
 * @projectName general-item
 * @description: 可重入锁测试
 * @date 2021/9/28/028 19:14
 */
public class ReentrantTest {
    //可重入就是说某个线程已经获得某个锁，可以再次获取锁而不会出现死锁。 （可以多次获取相同的锁，不发生死锁）。

    /*可重入锁有:
    synchronized（监听器模式，自动解锁监听器，使用形式不灵活，关联一个条件队列，非公平锁）
    ReentrantLock(依赖AQS,需要手动解锁，使用形式灵活（支持响应字段、超时、尝试获取锁），可关联多个条件队列，公平锁&非公平锁)*/

    public static void main(String[] args) {
        //synchronized重入锁效果测试
        //synchronizedLock();

        // ReentrantLock重入锁效果测试(需要手动解锁。测试解锁次数不够，重入锁失败测试)
        //reentrantLockTest1();

        //ReentrantLock重入锁效果测试(需要手动解锁。测试解锁次数一致，重入锁成功测试)
        reentrantLockTest2();
    }

    /**
     * synchronized重入锁效果测试（可自动解锁）
     */
    public static void synchronizedLock() {
        // 重入锁可以进入第二个synchronized,不产生死锁
        // 锁同一个对象
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    System.out.println("第一次获取锁，锁是："+this);
                }
                int index = 1;
                while (true) {
                    synchronized (this) {
                        System.out.println("第"+(++index)+"次获取到锁，锁是："+this);
                    }
                    if (index == 10) {
                        break;
                    }
                }
            }
        }).start();
       /* 输出结果：（synchronized自动解锁且。且可重入）
        第一次获取锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第2次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第3次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第4次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第5次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第6次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第7次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第8次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第9次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c
        第10次获取到锁，锁是：com.zlk.jdk.thread.lock.reentrant.ReentrantTest$1@233f585c*/
    }


    /**
     * ReentrantLock重入锁效果测试(需要手动解锁。测试解锁次数不够，重入锁失败测试)
     */
    public static void reentrantLockTest1() {
        // 锁同一个对象,不产生死锁（当前测试解锁次数不够导致失败情况）
        ReentrantLock lock = new ReentrantLock();
        // 第一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("第一次获取锁，锁是："+lock);
                    int index = 1;
                    while (true) {
                        try {
                            // 再次上锁
                            lock.lock();
                            System.out.println("第"+(++index)+"次获取到锁，锁是："+lock);
                            //随机休眠时间到达测试效果
                            Thread.sleep(new Random().nextInt(200));
                            if (index == 10) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            // 解除循环类的锁，当前测试不解锁（让加锁和解锁次数不一致效果）
                            //lock.unlock();
                        }

                    }
                } finally {
                    // 解除第一次加的锁
                    lock.unlock();
                }

            }
        }).start();

        // 再开一个线程做资源抢夺(看该线程是否获取到锁)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();

                    for (int i = 0; i < 20; i++) {
                        System.out.println("threadName:" + Thread.currentThread().getName());
                        try {
                            Thread.sleep(new Random().nextInt(200));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        //由于加锁次数和释放次数不一样，第二个线程始终无法获取到锁，导致一直在等待。
      /*  输出结果（只有第一个线程获取到锁，ReentrantLock加锁和解锁次数需要一致）
        第一次获取锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第2次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第3次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第4次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第5次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第6次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第7次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第8次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第9次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]
        第10次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@46a2a3bf[Locked by thread Thread-0]*/
    }


    /**
     * ReentrantLock重入锁效果测试(需要手动解锁。测试解锁次数一致，重入锁成功测试)
     */
    public static void reentrantLockTest2() {
        // 锁同一个对象,不产生死锁（当前测试解锁次数一致成功情况）
        ReentrantLock lock = new ReentrantLock();
        // 第一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("第一次获取锁，锁是："+lock);
                    int index = 1;
                    while (true) {
                        try {
                            // 再次上锁
                            lock.lock();
                            System.out.println("第"+(++index)+"次获取到锁，锁是："+lock);
                            //随机休眠时间到达测试效果
                            Thread.sleep(new Random().nextInt(200));
                            if (index == 10) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            // 解除循环类的锁，当前测试解锁（让加锁和解锁次数一致效果）
                            lock.unlock();
                        }

                    }
                } finally {
                    // 解除第一次加的锁
                    lock.unlock();
                }

            }
        }).start();

        // 再开一个线程做资源抢夺(看该线程是否获取到锁)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();

                    for (int i = 0; i < 20; i++) {
                        System.out.println("第2个线程，线程名称:" + Thread.currentThread().getName()+",锁是："+lock);
                        try {
                            Thread.sleep(new Random().nextInt(200));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        //由于加锁次数和释放次数一样，第二个线程可以获取到锁。
      /*  输出结果（两个线程获都取到锁，ReentrantLock加锁和解锁次数需要一致）
        第一次获取锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第2次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第3次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第4次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第5次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第6次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第7次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第8次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第9次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第10次获取到锁，锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-0]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]
        第2个线程，线程名称:Thread-1,锁是：java.util.concurrent.locks.ReentrantLock@ddbeecd[Locked by thread Thread-1]*/
    }
}
