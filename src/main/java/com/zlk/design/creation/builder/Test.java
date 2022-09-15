package com.zlk.design.creation.builder;

public class Test {
    public static void main(String[] args) {
        Worker worker = new Worker();
        //具体的构造类传给指挥者去控制具体产品的创建。
        RobotBuilder builder = new SmartRobotBuilderImpl();
        Robot robot = worker.builderRobot(builder);
        System.out.println("产品的head:"+robot.getHead());
    }
}
