package com.zlk.design.structure.bridge;

/**
 * 汽车抽象类(abstraction);注重对象的构造而不是创建
 *
 */
public abstract class Car {
    //将接口作为属性，弱关联解耦
    protected Color color;

    //通过set传入具体实现类。（类似于构造方法注入属性。弱关联解耦）
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * 生产
     * @param carType 汽车类型
     */
    public abstract void make(String carType);

}
