package com.zlk.jdk.genericity;

import com.zlk.po.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author likuan.zhou
 * @title: ClassOperation
 * @projectName practice
 * @description: 类的反射操作
 * @date 2021/9/2/002 10:40
 */
public class ReflexOperation<T> {

    /**
     * 字段名称获取字段值
     */
    private static  String getFieldVal(Object t, String fieldName) throws  IllegalAccessException, NoSuchFieldException{
        Class<?> zClass = t.getClass();
        //Object instance = zClass.newInstance();
        Field field = zClass.getDeclaredField(fieldName);
        //设置对象的访问权限，保证对private的属性的访问
        field.setAccessible(true);
        return (String) field.get(t);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        user.setId(1001L);
        user.setName("名字");
        ReflexOperation.getFieldVal(user,"name");
    }
}
