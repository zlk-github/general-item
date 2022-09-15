package com.zlk.design.structure.adapter;

/**
 * 汽车工厂实现类(没有具体品牌分类)
 */
public class CarFactoryImpl implements CarFactory {
    @Override
    public Car createCar() {
        Car car = new Car();
        car.setName("宝马");
        car.setColor("白色");
        return car;
    }
}
