package com.zlk.design.creation.abstractFactory;

/**
 * 热食类型工厂，实现产品族核心接口。完成各产品族热食物对象创建
 */
public class HostFoodFactory implements Factory{

    @Override
    public   Pizza createPizza(String type) {
        //使用反射取代太多的switch与if判断代码块。后面添加新产品类型，不在需要修改
        //Class类	代表类的实体，在运行的Java应用程序中表示类和接口
        //Field类	代表类的成员变量（成员变量也称为类的属性）
        //Method类	代表类的方法
        //Constructor类	代表类的构造方法
        Pizza pizza = null;
        try {
            //参考https://www.jianshu.com/p/9be58ee20dee
            //首字母转大写，用于找类。
            type = type.substring(0, 1).toUpperCase()+type.substring(1);
            System.out.println("type = [" + type + "]");
            //获取类Class。还可以使用
            Class<?> pizzaClass = Class.forName("com.zlk.design.creation.abstractFactory." + "Pizza" + type);
            //获取Class对应的类实例
            pizza  = (Pizza)pizzaClass.newInstance();
            pizza.prepare();
            pizza.make(); //热食需要加热制作
            pizza.complete();
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
      /*
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
            pizza.make(); //热食需要加热制作
            pizza.complete();
        }*/
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
            abstractMilk.make(); //热食需要加热制作
            abstractMilk.complete();
        }
        return abstractMilk;
    }


}
