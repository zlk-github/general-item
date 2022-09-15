package com.zlk.design.creation.factoryMethod;

public class Test {
    public static void main(String[] args) {
        PizzaStore1 pizzaStore1 = new PizzaStore1();
        pizzaStore1.orderPizza();
        System.out.println("----------------------------------");
        PizzaStore2 pizzaStore2 = new PizzaStore2();
        pizzaStore2.orderPizza();
    }
}
