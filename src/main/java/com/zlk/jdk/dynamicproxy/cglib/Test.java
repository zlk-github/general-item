package com.zlk.jdk.dynamicproxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

import java.util.List;

/**
 * @Description: 测试cglib动态代理;  需要单独引入包，被代理对象不需要强制实现接口
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 14:46
 */
public class Test {

    public static void main(String[] args) {
        CglibProxy cglibProxy  = new CglibProxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(cglibProxy);

        //enhancer.create() 创建被代理对象实例
        UserService userService = (UserService) enhancer.create();

        //调用方法queryUserList()
        List<User> users = userService.queryUserList(new User());
        System.out.println(users);

        System.out.println("=============");

        //调用方法queryUserById()
        User user = userService.queryUserById(1001L);
        System.out.println(user);
    }
}
