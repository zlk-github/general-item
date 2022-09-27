package com.zlk.design.creation.simpleFactory;

/**
 * 客户类
 */
public class PizzaStore {
    private  SimplePizzaFactory simplePizzaFactory;

    /**
     * 构造方法创建，创建PizzaStore传入参数创建simplePizzaFactory
     * @param simplePizzaFactory 工厂实例
     */
    public PizzaStore(SimplePizzaFactory simplePizzaFactory){
        this.simplePizzaFactory = simplePizzaFactory;
    }
    /**
     *
     * @param type 披萨类型
     */
    public void orderPizza(String type){
        Pizza pizza = simplePizzaFactory.createPizza(type);
        pizza.prepare();
        pizza.make();
        pizza.complete();
    }
}
