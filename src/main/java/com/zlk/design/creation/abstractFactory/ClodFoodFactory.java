package com.zlk.design.creation.abstractFactory;

/**
 * 冷食类型工厂，实现产品族核心接口。完成各产品族冷食物对象创建
 */
public class ClodFoodFactory implements Factory{

    @Override
    public Pizza createPizza(String type) {
        Pizza pizza = null;
        switch (type){
            case "vegg":
                pizza = new PizzaVegg();
                break;
            case "meet":
                pizza = new PizzaVegg();
                break;
            default:
                System.out.println("当前没有该类型披萨，你输入类型为："+type);
                break;
        }

        if(pizza!=null){
            pizza.prepare();
            //pizza.make(); 冷食不需要加热制作
            pizza.complete();
        }
        return pizza;
    }

    @Override
    public AbstractMilk createMilk(String type) {
        AbstractMilk abstractMilk = null;
        switch (type){
            case "apple":
                abstractMilk = new AppleMilk();
                break;
            default:
                System.out.println("当前没有该类型披萨，你输入类型为："+type);
                break;
        }

        if(abstractMilk !=null){
            abstractMilk.prepare();
            //pizza.make(); 冷食不需要加热制作
            abstractMilk.complete();
        }
        return abstractMilk;
    }


}
