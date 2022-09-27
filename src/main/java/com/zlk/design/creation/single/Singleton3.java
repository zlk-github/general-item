package com.zlk.design.creation.single;

/**
 * 静态内部类实现
 */
public class Singleton3 {
    public static Singleton3 singleton = null;

    //构造函数私有，不允许外部使用new创建实例
    private Singleton3(){
    }

    /**
     * 线程安全
     * @return 类实例
     */
    public static Singleton3 getInstance(){
       return Inner.instance;
    }

    private static class Inner{
        private final static Singleton3 instance = new Singleton3();
    }
}
