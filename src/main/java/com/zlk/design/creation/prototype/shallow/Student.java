package com.zlk.design.creation.prototype.shallow;

import java.util.ArrayList;
import java.util.Date;

/**
 * 浅克隆使用.clone()方法进行克隆。克隆的对象必须实现java.lang.Cloneable接口。重写clone()方法。
 */
public class Student implements Cloneable {
    private String name;
    private Date birthday;
    private Integer age;

    //以下两个属性需要特别关注
    private School school;
    private ArrayList<String> friends;

    /**
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Student clone() throws CloneNotSupportedException {
        //方式一 浅克隆
        //return (Student)super.clone();

        //方式二 普通深克隆  对非java的引用对象（如list,array等）。可以实现java.lang.Cloneable接口。重写clone()方法。
        // 通过以下实现克隆，但是修改较大。
        Student student = (Student) super.clone();
        student.school= school.clone();
        school.clone();
        return student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        //只是为了测试
        return "Student{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", school=" + school.getName() +
                ", friends=" + friends.get(0) +
                ", friendsSize=" + friends.size() +
                '}';
    }

}
