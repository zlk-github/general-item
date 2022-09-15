package com.zlk.design.structure.bridge;

/**
 * 黑色实现类
 */
public class Black implements Color{
    @Override
    public void addColor(String colorType) {
        System.out.println("Black颜色:" + colorType);
    }
}
