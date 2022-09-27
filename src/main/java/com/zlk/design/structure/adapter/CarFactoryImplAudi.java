package com.zlk.design.structure.adapter;

/**
 * 奥迪汽车工厂
 */
public class CarFactoryImplAudi implements CarFactory{
    @Override
    public Car createCar() {
        Car car = new Car();
        car.setName("奥迪");
        car.setColor("白色");
        return car;
    }
}
