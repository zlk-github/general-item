package com.zlk.jdk.dynamicproxy.jdk;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @Description: 测试jdk原生动态代理;  被代理对象必须有统一实现的接口
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 14:46
 */
public class Test {

    public static void main(String[] args) {
        //需要被代理的对象。该类必须有接口实现
        UserService userService = new UserServiceImpl();
        //注入被代理对象
        MyInvocationHandler<User>  invocationHandler = new MyInvocationHandler<>(userService);

        //Proxy.newProxyInstance（）生成被代理实例对象（参数：接口ClassLoader，实现类，代理器）
        UserService userServiceProxy = (UserService) Proxy.newProxyInstance(UserService.class.getClassLoader(), userService.getClass().getInterfaces(), invocationHandler);

        //调用方法queryUserList()
        List<User> users = userServiceProxy.queryUserList(new User());
        System.out.println(users);

        System.out.println("=============");

        //调用方法queryUserById()
        User user = userServiceProxy.queryUserById(1001L);
        System.out.println(user);
    }
}
