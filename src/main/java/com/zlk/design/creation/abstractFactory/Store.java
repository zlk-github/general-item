package com.zlk.design.creation.abstractFactory;

/**
 * 购买类
 */
public class Store {

    /**
     * 消费
     * @param foodType 产品族（牛奶milk、披萨pizza）
     * @param proType 产品类型（那种牛奶或者那种披萨）
     * @param type   种类（冷的clod或者热的host）
     */
    public static void consume(String foodType, String proType , String type){
         if ("pizza".equals(foodType)){
              if ("clod".equals(type)){
                  if ("vegg".equals(proType)){
                      ClodFoodFactory clodFoodFactory = new ClodFoodFactory();
                      clodFoodFactory.createPizza("vegg");
                  }
              }
              if ("host".equals(type)){
                  if ("vegg".equals(proType)){
                      HostFoodFactory hostFoodFactory = new HostFoodFactory();
                      hostFoodFactory.createPizza("vegg");
                  }
              }
         }
    }

    public static void main(String[] args) {
        consume("pizza","vegg","host");
    }
}
