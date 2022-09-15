package com.zlk.design.creation.builder;

/**
 * Director 指挥官，指挥具体的类创建
 */
public class Worker {
    /**
     * 传入需要创建的构造者生产具体的产品实例
     * @param builder 具体构造者
     */
    public Robot builderRobot(RobotBuilder builder){
        //调用具体构造方法中的方法对 对象进行具体创建
        builder.createhead();
        builder.createbody();
        builder.createLegs();
        return builder.createRobot();
    }
}
