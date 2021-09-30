## AQS原理

Java中的大部分同步类（Lock、Semaphore、ReentrantLock等）都是基于AbstractQueuedSynchronizer（简称为AQS）实现的。AQS是一种提供了原子式管理同步状态、阻塞和唤醒线程功能以及队列模型的简单框架。

AQS：AbstractQuenedSynchronizer抽象的队列式同步器。是除了java自带的synchronized关键字之外的锁机制。

AQS的全称为（AbstractQueuedSynchronizer），这个类在java.util.concurrent.locks包

AQS的核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并将共享资源设置为锁定状态，如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制AQS是用CLH队列锁实现的，即将暂时获取不到锁的线程加入到队列中。

CLH（Craig，Landin，and Hagersten）队列是一个虚拟的双向队列，虚拟的双向队列即不存在队列实例，仅存在节点之间的关联关系。

AQS是将每一条请求共享资源的线程封装成一个CLH锁队列的一个结点（Node），来实现锁的分配。

**实现了AQS的锁有：自旋锁、互斥锁、读锁写锁、条件产量、信号量、栅栏都是AQS的衍生物**

### 1 ReentrantLock特性概览

    ReentrantLock意思为可重入锁，指的是一个线程能够对一个临界资源重复加锁。

|     | **ReentrantLock**  | **Synchronized** 
|  ----  | ----  | ----  |
| 锁实现机制  | 依赖AQS | 监听器模式 |
| 灵活性  | 支持响应字段、超时、尝试获取锁 |不灵活 |
| 释放形式  | 必须显示调用unlock()释放锁 |自动释放监听器 |
| 锁类型  | 公平锁&非公平锁 |非公平锁 |
| 条件队列  | 可关联多个条件队列 |关联一个条件队列 |
| 可重入性  | 可重入 |可重入 |

````java
// **************************Synchronized的使用方式**************************
// 1.用于代码块
synchronized (this) {}
// 2.用于对象
synchronized (object) {}
// 3.用于方法
public synchronized void test () {}
// 4.可重入
for (int i = 0; i < 100; i++) {
	synchronized (this) {}
}
// **************************ReentrantLock的使用方式**************************
//ReentrantLock加锁和解锁次数必须一样多。
public void test () throw Exception {
	// 1.初始化选择公平锁true、非公平锁
	ReentrantLock lock = new ReentrantLock(true);
	// 2.可用于代码块
	lock.lock();
	try {
		try {
			// 3.支持多种加锁方式，比较灵活; 具有可重入特性
			if(lock.tryLock(100, TimeUnit.MILLISECONDS)){ }
		} finally {
			// 4.手动释放锁
			lock.unlock()
		}
	} finally {
		lock.unlock();
	}
}
````

AQS 定义了两种资源共享方式：
1.Exclusive：独占，只有一个线程能执行，如ReentrantLock
2.Share：共享，多个线程可以同时执行，如Semaphore、CountDownLatch、ReadWriteLock，CyclicBarrier

### 参考
    
      可重入锁： https://blog.csdn.net/w8y56f/article/details/89554060
      
      AQS: https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html

      公平锁与非公平锁： https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651749434&idx=3&sn=5ffa63ad47fe166f2f1a9f604ed10091&chksm=bd12a5778a652c61509d9e718ab086ff27ad8768586ea9b38c3dcf9e017a8e49bcae3df9bcc8&scene=38#wechat_redirect

      可重入锁（清晰）https://blog.csdn.net/mulinsen77/article/details/84583716

      锁(全) https://blog.csdn.net/qq_50695280/article/details/115099872