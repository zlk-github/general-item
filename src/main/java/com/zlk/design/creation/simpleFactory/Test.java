package com.zlk.design.creation.simpleFactory;

/**
 * 测试类
 */
public class Test {
    public static void main(String[] args) {
        SimplePizzaFactory factory = new SimplePizzaFactory();
        PizzaStore pizzaStore = new PizzaStore(factory);
        pizzaStore.orderPizza("meet");
        System.out.println("---------");
        pizzaStore.orderPizza("vegg");
    }
}
