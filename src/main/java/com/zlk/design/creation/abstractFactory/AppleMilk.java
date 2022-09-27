package com.zlk.design.creation.abstractFactory;

/**
 * 苹果牛奶
 */
public class AppleMilk extends AbstractMilk {
    @Override
    public void prepare() {
        System.out.println("准备牛奶");
    }

    @Override
    public void make() {
        System.out.println("加热牛奶");
    }

    @Override
    public void complete() {
        System.out.println("制作牛奶完成");
    }
}
