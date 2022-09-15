package com.zlk.design.structure.adapter;

/**
 * 适配器接口（类似USB的适配）
 */
public interface CarAdapter {

    /**
     * 改变汽车颜色
     * @param car 那个类型汽车
     * @param color 改为什么颜色
     * @return 汽车对象
     */
    Car changeColor(Car car,String color);
}
