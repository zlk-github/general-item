package com.zlk.jdk.thread.threadLocal;

/**
 *  ThreadLocal:每个线程独有本地变量，解决多线程使用同一变量导致的线程安全问题。
 *      对每个线程维护一个ThreadLocalMap。ThreadLocal用于管理线程与ThreadLocalMap关系。
 *      一个线程对应一个ThreadLocalMap，ThreadLocalMap内容是一个数组table（table[i] = new Entry(firstKey, firstValue)）
 *      ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
 *             table = new Entry[INITIAL_CAPACITY];
 *             int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
 *             table[i] = new Entry(firstKey, firstValue);
 *             size = 1;
 *             setThreshold(INITIAL_CAPACITY);
 *         }
 *      数据应用：如多数据源切换。
 *
 * @author likuan.zhou
 * @date 2022/3/28/028 8:49
 */
public class ThreadLocalTest {
    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    static void print(String key) {
        // 获取当前线程对应本地变量
        System.out.println("threadLocal = " + threadLocal.get());
        // 移除当前线程对应本地变量
        //threadLocal.remove();
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 线程1中设置本地变量
                threadLocal.set("threadLocal1");
                threadLocal.set("threadLocal3");
                // 调用打印，并移除
                print("threadLocal1");
                System.out.println("（threadLocal1）ThreadLocal after remove:"+threadLocal.get());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 线程2中设置本地变量
                threadLocal.set("threadLocal2");
                // 调用打印，并移除
                print("threadLocal2");
                System.out.println("（threadLocal2）ThreadLocal after remove:"+threadLocal.get());
            }
        });

        thread1.start();
        thread2.start();
        /**
         * 输出结果
         */
    }

}

