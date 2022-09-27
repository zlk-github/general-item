package com.zlk.design.structure.adapter;

/**
 * 类适配器模式（继承具体工厂并实现适配器接口）
 * 由于实用继承只能对单个工厂有效。假如有不同品牌的汽车工厂后将不能实现
 * （总不能每个品牌加一个适配器实现类，虽然能实现，但是代码量大。且不通用）。
 */
public class AdapterClassImpl extends CarFactoryImpl implements CarAdapter {
    @Override
    public Car changeColor(Car car, String color) {
        car.setColor(color);
        return car;
    }
}
