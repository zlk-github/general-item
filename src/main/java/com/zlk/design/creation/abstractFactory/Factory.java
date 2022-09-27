package com.zlk.design.creation.abstractFactory;


/**
 * 工厂接口，抽象出产品族（产品种类，如面包、牛奶、披萨等）各自的核心创建方法。
 * --该接口只负责产品族创建的抽象
 */
public interface Factory {

    /**
     * 创建披萨对象抽象方法
     * @param type 披萨类型
     * @return 具体披萨对象
     */
     Pizza createPizza(String type);

    /**
     * 创建牛奶对象抽象方法
     * @param type 牛奶类型
     * @return 具体牛奶对象
     */
    AbstractMilk createMilk(String type);
}