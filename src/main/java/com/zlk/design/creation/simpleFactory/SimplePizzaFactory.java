package com.zlk.design.creation.simpleFactory;

/**
 * 工厂类，用于生产各种披萨
 * 通过传入参数生成对应的披萨对象。（多态）
 * --当有新的子类创建时，只需要子类继承父类，在工厂方法加一个判断分支即可。
 */
public class SimplePizzaFactory {

    /**
     * 创建披萨对象
     * @param type 披萨种类
     * @return 披萨对象
     */
    public Pizza createPizza(String type){
        if("meet".equals(type)){
            return new PizzaMeet();
        }
        if("vegg".equals(type)){
            return new PizzaVegg();
        }
        return null;
    }
}
