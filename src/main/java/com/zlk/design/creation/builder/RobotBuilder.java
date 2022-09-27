package com.zlk.design.creation.builder;

/**
 * Builder 机器人抽象构造者接口，相当于工厂设计模式的抽象工厂。
 * 区别在于：工厂设计模式关注具体类创建，不关注类创建细节。建造者模式更关注创建类的细节。
 */
public interface RobotBuilder {
    //细节，字段复制的来源，组装形式等。如果是对象将更为复杂
    public void createhead();
    //细节
    public void createbody();
    //细节
    public void createLegs();

    /**
     * 相同点：工厂设计模式与构造者模式相同点都是用来创建对象
     * 不同点：工厂设计模式关注具体类创建，不关注类创建细节（工厂模式只有一个创建方法）。建造者模式更关注创建类的细节（比工厂模式多了字段的具体创建方法）。
     * @return Robot 具体对象
     */
    public Robot createRobot();

}
