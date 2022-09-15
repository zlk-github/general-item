package com.zlk.design.structure.adapter;

/**
 * 宝马工厂
 */
public class CarFactoryImplBM implements CarFactory{
    @Override
    public Car createCar() {
        Car car = new Car();
        car.setName("宝马");
        car.setColor("白色");
        return car;
    }
}
