package com.zlk.design.structure.adapter;

/**
 * 汽车抽象工厂
 */
public interface CarFactory {
    /**
     * 创建汽车方法
     * @return 汽车对象
     */
     Car createCar();
}
