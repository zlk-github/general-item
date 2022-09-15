package com.zlk.design.structure.bridge;


public class Test {
    public static void main(String[] args) {

        Black black = new Black();
        CarImpl car = new CarImpl();
        //注入color
        car.setColor(black);
        car.make("黑色");
        System.out.println(car.color);


        Blue blue = new Blue();
        CarImpl car1 = new CarImpl();
        //注入color
        car1.setColor(blue);
        car1.make("蓝色");
        System.out.println(car1.color);

    }


}
