package com.zlk.design.creation.factoryMethod;


/**
 * 工厂接口，抽象出工程类创建方法。
 * --当有新的产品子类创建时，只需要添加一个具体类去实现工厂接口就行。不需要修改原工厂。但是会多出很多工厂实现了类。
 */
public interface Factory {

    /**
     * 创建披萨对象抽象方法
     * @return 披萨对象
     */
    public Pizza createPizza();
}