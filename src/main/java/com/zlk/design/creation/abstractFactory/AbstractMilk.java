package com.zlk.design.creation.abstractFactory;

/**
 * 牛奶对象抽象类,供牛奶子类继承
 */
public abstract class AbstractMilk {
    /**
     * 准备
     */
    public abstract void prepare();

    /**
     * 制作
     */
    public abstract void make();

    /**
     * 完成
     */
    public abstract void complete();
}
