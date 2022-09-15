package com.zlk.design.creation.prototype.shallow;

import java.util.ArrayList;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {
        Student stu1 = new Student();
        stu1.setName("王五");
        stu1.setBirthday(new Date(0));
        stu1.setAge(18);
        School school = new School();
        school.setName("伦敦大学");
        stu1.setSchool(school);
        ArrayList<String> array = new ArrayList<>();
        array.add("李四");
        stu1.setFriends(array);

        //______________浅克隆（克隆的是地址引用）,修改对象属性需要不改变原来对象实现克隆，需要重新new 对象。不建议这么做_______________________
        Student stu2 = stu1.clone();
        stu2.setName("Tom");//String是fianl类型，这个地方相当于new String。stu1不变,stu2改变成功。
        stu2.setBirthday(new Date());//new Date。stu1不变,stu2改变成功。
        stu2.setAge(19);//final修饰，效果和基本类型相似，stu1不变,stu2改变成功。。
        stu2.getSchool().setName("哈佛大学");//对象引用，没有new对象。导致stu1改变，stu2改变成功。造成原对象出问题。
        stu2.getFriends().add("李六");//对象引用，没有new list.导致直接在原来list上进行了add。

        System.out.println(stu1.toString());
        System.out.println("==============");
        System.out.println(stu2.toString());
    }
}
