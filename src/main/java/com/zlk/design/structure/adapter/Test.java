package com.zlk.design.structure.adapter;

public class Test {

    public static void main(String[] args) {
        CarFactoryImpl factory = new CarFactoryImpl();
        //假设这时候需要创建别的颜色不能适配
        //解决方法1：在CarFactory工厂接口中加改变颜色的方法，相关的实现类都需要改动实现这个方法。改动大且违反开闭原则。
        //解决方案2：单独对需要加颜色的产品加一个工厂来实现改变颜色，但是导致工厂不通用 。
        //解决方案3：适配器模式（1类适配器-需要实现适配器接口并继承具体工厂实现类，但是类不能多继承，不实用；
        //                   2接口适配器-需要实现适配器接口，但是具体工厂作为属性进行弱关联）
        Car car = factory.createCar();
        System.out.println(car.getName());
        System.out.println(car.getColor());

        System.out.println("------------类适配器模式----------");
        //类适配器模式
        AdapterClassImpl adapterClass = new AdapterClassImpl();
        Car car1 = adapterClass.changeColor(car, "红色");
        System.out.println(car1.getName());
        System.out.println(car1.getColor());

        System.out.println("----------对象适配器模式------------");
        //对象适配器模式
        //奥迪改变颜色
        AdapterObjectImpl adapterObject = new AdapterObjectImpl();
        Car audi = adapterObject.createCar("audi");
        adapterObject.changeColor(audi, "蓝色");
        System.out.println(audi.getName());
        System.out.println(audi.getColor());
        System.out.println();

        //宝马改变颜色
        AdapterObjectImpl adapterObject1 = new AdapterObjectImpl();
        Car bm = adapterObject1.createCar("BM");
        adapterObject1.changeColor(bm, "蓝色");
        System.out.println(bm.getName());
        System.out.println(bm.getColor());

        System.out.println("----------------------");

    }
}
