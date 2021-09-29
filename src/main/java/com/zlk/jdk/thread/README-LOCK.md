## 常见锁java

      volatile
      单机锁LOCK，synchronized
      分布式锁：redis,zookpeer,mysql等实现
      乐观锁CAS等
      多线程atomicInteger与分段锁 （https://www.cnblogs.com/muzhongjiang/p/15142938.html）


### 2 锁(需要深入了解使用和原理)

#### 2.1 可重入锁（JAVA中使用多）

**测试代码见**:  [可重入锁](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README.md#可重入锁)

#### 2.1.1 什么是可重入锁

    可重入就是说某个线程已经获得某个锁，可以再次获取锁而不会出现死锁。 （可以多次获取相同的锁，不发生死锁）。

可重入锁有

    synchronized
    ReentrantLock

    注意：
      ReentrantLock 和 synchronized 不一样，需要手动释放锁，
      所以使用 ReentrantLock的时候一定要手动释放锁，并且加锁次数和释放次数要一样。
      （ReentrantLock加锁次数和释放次数不一样，第二个线程始终无法获取到锁，导致一直在等待。）

#### 2.1.2 可重入锁作用

    避免死锁，解决线程多次进入锁内执行任务。（如数据库事务）。

#### 2.1.2 可重入锁原理

#### 2.2 不可重入锁

### 3 aqs原理

Java中的大部分同步类（Lock、Semaphore、ReentrantLock等）都是基于AbstractQueuedSynchronizer（简称为AQS）实现的。AQS是一种提供了原子式管理同步状态、阻塞和唤醒线程功能以及队列模型的简单框架。

#### 3.1 ReentrantLock特性概览

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
	// 1.初始化选择公平锁、非公平锁
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

### 参考
    
      可重入锁： https://blog.csdn.net/w8y56f/article/details/89554060
      
      AQS: https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html

      公平锁与非公平锁： https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651749434&idx=3&sn=5ffa63ad47fe166f2f1a9f604ed10091&chksm=bd12a5778a652c61509d9e718ab086ff27ad8768586ea9b38c3dcf9e017a8e49bcae3df9bcc8&scene=38#wechat_redirect