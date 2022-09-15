package com.zlk.design.creation.builder;

/** product:产品角色，具体的产品对象
 *  机器人类
 */
public class Robot {
    //实际使用时字段可能定义的对象，需要复杂的组装。
    private String head;
    private String body;
    private String legs;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLegs() {
        return legs;
    }

    public void setLegs(String legs) {
        this.legs = legs;
    }
}
