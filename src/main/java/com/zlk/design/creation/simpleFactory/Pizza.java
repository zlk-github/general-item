package com.zlk.design.creation.simpleFactory;

/**
 * Pizza类是一个抽象父类。抽象出所有披萨子类共有方法。
 * 局限性，个别种类特有方法没法添加（创建工厂时使用多态）。
 */
public abstract class Pizza {
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
