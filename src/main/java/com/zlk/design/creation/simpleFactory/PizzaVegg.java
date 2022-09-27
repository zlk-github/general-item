package com.zlk.design.creation.simpleFactory;

/**
 * 蔬菜披萨
 */
public class PizzaVegg extends Pizza{
    @Override
    public void prepare() {
        System.out.println("准备Vegg");
    }

    @Override
    public void make() {
        System.out.println("制作蔬菜披萨");
    }

    @Override
    public void complete() {
        System.out.println("蔬菜披萨制作完成");
    }
}
