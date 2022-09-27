package com.zlk.design.creation.prototype.deep;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * 深克隆直接复制成员变量,需要实现序列化Cloneable。使用序列化与反序列化读IO流
 */
public class Student implements Serializable  {
    private String name;
    private Date birthday;
    private Integer age;

    //以下两个属性需要特别关注
    private School school;
    private ArrayList<String> friends;

    /**
     *
     * @return
     * @throws Exception
     */
    public Student deepClone()  {
        //方式三 深度克隆
        Student student = null;
        try {
            //序列化将对象写道IO流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(this);

            //从IO流将对象反序列化出来
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(in);
            student = (Student)oi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return student ;
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
