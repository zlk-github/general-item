package com.zlk.design.creation.single;

/**
 * 双重检索
 */
public class Singleton2 {
    //必须volatile修饰
    public static volatile Singleton2 singleton = null;

    //构造函数私有，不允许外部使用new创建实例
    private Singleton2(){
    }

    /**
     * 线程安全
     * @return 类实例
     */
    public static Singleton2 getInstance(){
        //没有同步会出现线程不安全
        if(singleton == null){
            synchronized (Singleton2.class){
                if(singleton == null){
                    singleton = new Singleton2();
                }
            }
        }
        return singleton;
    }
}
