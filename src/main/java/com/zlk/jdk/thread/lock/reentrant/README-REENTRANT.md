## 可重入锁

      volatile
      单机锁LOCK，synchronized
      分布式锁：redis,zookpeer,mysql等实现
      乐观锁CAS等
      多线程atomicInteger与分段锁 （https://www.cnblogs.com/muzhongjiang/p/15142938.html）


### 2 锁(需要深入了解使用和原理)

#### 2.1 可重入锁（JAVA中使用多）

**测试代码见**:  [可重入锁](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/lock/reentrant/ReentrantTest.java#可重入锁)

#### 2.1.1 什么是可重入锁

    可重入就是说某个线程已经获得某个锁，可以再次获取锁而不会出现死锁。 （可以多次获取相同的锁，不发生死锁）。

可重入锁有

    synchronized
    ReentrantLock

    注意：
      ReentrantLock 和 synchronized 不一样，需要手动释放锁，
      所以使用 ReentrantLock的时候一定要手动释放锁，并且加锁次数和释放次数要一样。
      （ReentrantLock加锁次数和释放次数不一样，第二个线程始终无法获取到锁，导致一直在等待。）

ReentrantLock意思为可重入锁，指的是一个线程能够对一个临界资源重复加锁。

|     | **ReentrantLock**  | **Synchronized**
|  ----  | ----  | ----  |
| 锁实现机制  | 依赖AQS | 监听器模式 |
| 灵活性  | 支持响应字段、超时、尝试获取锁 |不灵活 |
| 释放形式  | 必须显示调用unlock()释放锁 |自动释放监听器 |
| 锁类型  | 公平锁&非公平锁 |非公平锁 |
| 条件队列  | 可关联多个条件队列 |关联一个条件队列 |
| 可重入性  | 可重入 |可重入 |


#### 2.1.2 可重入锁作用

    避免死锁，解决线程多次进入锁内执行任务。（如数据库事务）。

#### 2.1.3 可重入锁原理

#### 2.1.4 可重入锁测试

可重入锁有:

    synchronized（监听器模式，自动解锁监听器，使用形式不灵活，关联一个条件队列，非公平锁）
    
    ReentrantLock(依赖AQS,需要手动解锁(加锁和解锁次数要一致)，使用形式灵活（支持响应字段、超时、尝试获取锁），可关联多个条件队列，公平锁&非公平锁)*/

测试代码如下：

````java
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
````



#### 2.2 不可重入锁


### 3 aqs原理

AQS原理详见：[AQS原理](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/lock/README-AQS.md#AQS原理)

    Java中的大部分同步类（Lock、Semaphore、ReentrantLock等）都是基于AbstractQueuedSynchronizer（简称为AQS）实现的。AQS是一种提供了原子式管理同步状态、阻塞和唤醒线程功能以及队列模型的简单框架。
    
    AQS：AbstractQuenedSynchronizer抽象的队列式同步器。是除了java自带的synchronized关键字之外的锁机制。
    
    AQS的全称为（AbstractQueuedSynchronizer），这个类在java.util.concurrent.locks包
    
    AQS的核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并将共享资源设置为锁定状态，如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制AQS是用CLH队列锁实现的，即将暂时获取不到锁的线程加入到队列中。
    
    CLH（Craig，Landin，and Hagersten）队列是一个虚拟的双向队列，虚拟的双向队列即不存在队列实例，仅存在节点之间的关联关系。
    
    AQS是将每一条请求共享资源的线程封装成一个CLH锁队列的一个结点（Node），来实现锁的分配。
    
    **实现了AQS的锁有：自旋锁、互斥锁、读锁写锁、条件产量、信号量、栅栏都是AQS的衍生物**


### 参考
    
      可重入锁： https://blog.csdn.net/w8y56f/article/details/89554060
      
      AQS: https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html

      公平锁与非公平锁： https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651749434&idx=3&sn=5ffa63ad47fe166f2f1a9f604ed10091&chksm=bd12a5778a652c61509d9e718ab086ff27ad8768586ea9b38c3dcf9e017a8e49bcae3df9bcc8&scene=38#wechat_redirect

      可重入锁（清晰）https://blog.csdn.net/mulinsen77/article/details/84583716

      锁(全) https://blog.csdn.net/qq_50695280/article/details/115099872