package com.zlk.design.creation.prototype.deep;


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

        //深度克隆，复制前后对象互不影响
        Student stu2 = stu1.deepClone();
        stu2.setName("Tom");
        stu2.setBirthday(new Date());
        stu2.setAge(19);
        stu2.getSchool().setName("哈佛大学");
        stu2.getFriends().add("李六");

        System.out.println(stu1.toString());
        System.out.println("==============");
        System.out.println(stu2.toString());
    }
}
