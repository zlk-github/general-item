package com.zlk.design.creation.factoryMethod;


/**
 * 购蔬菜披萨类，实现工厂接口
 */
public class PizzaStore2 implements Factory {

    /**
     * 购买披萨
     */
    public void orderPizza(){
        Pizza pizza = createPizza();
        pizza.prepare();
        pizza.make();
        pizza.complete();
    }

    /**
     * 自己的产品创建有自己的工厂实现类完成。当新增产品时，不会影响别的产品与原工厂。
     * @return Pizza具体产品
     */
    @Override
    public Pizza createPizza() {
        return new PizzaVegg();
    }
}
