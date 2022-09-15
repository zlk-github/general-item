package com.zlk.design.structure.bridge;

/**
 * 汽车实现类
 */
public class CarImpl extends Car{

    @Override
    public void make(String carType) {
        //Car引入了color。所以可以直接调用color的属性与方法
        color.addColor(carType);
    }
}
