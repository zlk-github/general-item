package com.zlk.design.creation.single;

/**
 * 饿汉式
 * 声明变量时就开始实例化。虽然线程安全，但是会影响系统启动时间。
 */
public class Singleton1 {
    //饿汉式
    public static final Singleton1 singleton1 = new Singleton1();

    //构造函数私有，不允许外部使用new创建实例
    private Singleton1(){
    }

    /**
     * 公有静态方法提供唯一的类实例
     * @return 类实例
     */
    public static Singleton1 getInstance(){
        return singleton1;
    }
}
