package com.zlk.design.structure.bridge;

/**
 * 蓝色实现类
 */
public class Blue implements Color{

    @Override
    public void addColor(String colorType) {
        System.out.println("Blue颜色:" + colorType);
    }
}
