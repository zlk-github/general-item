package com.zlk.design.creation.factoryMethod;

/**
 * 肉类披萨
 */
public class PizzaMeet extends Pizza {
    @Override
    public void prepare() {
        System.out.println("准备Meet");
    }

    @Override
    public void make() {
        System.out.println("制作肉类披萨");
    }

    @Override
    public void complete() {
        System.out.println("肉类披萨制作完成");
    }
}
