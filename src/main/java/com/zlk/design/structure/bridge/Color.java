package com.zlk.design.structure.bridge;

/**
 * 桥接模式不同的地方，将属性抽象成接口。作为car的一个属性
 */
public interface Color {
    /**
     * 添加颜色
     * @param colorType 颜色
     */
    void addColor(String colorType);
}
