package com.zlk.design.creation.builder;

/**
 * ConcreteBuilder具体构造者类，实现抽象的构造者接口。进行类的具体组装并返回类实例。
 */
public class SmartRobotBuilderImpl implements RobotBuilder {
    Robot robot = new Robot();
    @Override
    public void createhead() {
        //可能有许多的逻辑与工艺才能得到该值
        String head = "Head";
        String smartChip = "Smart";
        //将其装载，**别人调用不在需要了解具体细节**。
        String com = head + smartChip;
        robot.setHead(com);
    }

    @Override
    public void createbody() {
        robot.setBody("body");
    }

    @Override
    public void createLegs() {
        String leg = "Legs";
        String cutLeg = "Sinele";
        String finallegs = leg + cutLeg;
        robot.setLegs(finallegs);
    }

    @Override
    public Robot createRobot() {
         return robot;
    }
}
