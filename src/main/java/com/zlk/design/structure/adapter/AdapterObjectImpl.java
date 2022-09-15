package com.zlk.design.structure.adapter;

/**
 * 接口适配器-需要实现适配器接口，但是具体工厂作为属性进行弱关联。
 *          优点：通用且便于扩展。
 *          缺点：一定程度增加了代码的可读性与复杂性。
 */
public class AdapterObjectImpl implements CarAdapter{
    //具体工厂作为属性。由继承的强关联变为属性的弱关联
    private CarFactoryImplAudi audi;
    private CarFactoryImplBM bm;

    /**
     * 创建汽车方法
     * @return 汽车对象
     */
    public Car createCar(String carType){
        Car car  = null;
        if("audi".equals(carType)){
            audi = new CarFactoryImplAudi();
            car = audi.createCar();
        }
        if("BM".equals(carType)){
            bm = new CarFactoryImplBM();
            car = bm.createCar();
        }
        return car;
    }

    @Override
    public Car changeColor(Car car, String color) {
        car.setColor(color);
        return car;
    }
}
