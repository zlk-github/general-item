package com.zlk.design.creation.single;

/**
 * 懒汉式
 * 获取时再判断是否需要实例化。原生的获取线程不安全，可以加锁实现线程安全。推荐使用
 */
public class Singleton {
    //饿汉式
    public static  Singleton singleton = null;

    //构造函数私有，不允许外部使用new创建实例
    private Singleton(){}

    /**
     * 线程不安全
     * 公有静态方法提供唯一的类实例（没加同步，线程同时进来时。初始判断时的时间差可能产生多个实例。）
     * @return 类实例
     */
    public static Singleton getInstance(){
        //没有同步会出现线程不安全
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }

    /**
     * 线程安全
     * 公有静态方法提供唯一的类实例（方法加同步，但是因为加在方法上，影响访问效率）
     * @return 类实例
     */
    public static synchronized Singleton getInstance1(){
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}
