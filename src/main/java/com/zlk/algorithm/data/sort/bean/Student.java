package com.zlk.algorithm.data.sort.bean;

import lombok.Getter;
import lombok.Setter;

/**学生类
 * @author  zhoulk
 * date: 2019/5/17 0017
 */
@Getter
@Setter
public class Student implements Comparable{
    /**姓名*/
    private String name;
    /**年龄*/
    private Integer age;

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if(this.age.compareTo(((Student)o).age)>0){
            return 1;
        }
        if(this.age.compareTo(((Student)o).age)==0){
            if(this.name.compareTo(((Student)o).name)>0){
                return 1;
            }
        }
        return 0;
    }
}
