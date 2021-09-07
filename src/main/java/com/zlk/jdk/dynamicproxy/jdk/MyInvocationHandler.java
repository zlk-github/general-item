package com.zlk.jdk.dynamicproxy.jdk;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description: jdk方式实现动态代理器-需要实现InvocationHandler接口，重写invoke方法。
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 10:46
 */
@Slf4j
public class MyInvocationHandler<T> implements InvocationHandler {
    /**被代理对象,实现统一接口的实现类（UserServiceImpl）*/
    private  Object target;

    /**
     * 构造方法注入被代理对象
     * @param target
     */
    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // proxy 为MyInvocationHandler
        // method 当前被代理对象执行的具体方法
        // args 当前被代理对象执行的具体方法的入参

        //每次调用日志会输出两次

        //前置处理，条件处理、字段校验、安全等
        if( args != null &&args[0] instanceof User){

            log.info("前置处理：条件处理、字段校验、安全等");
        }

        //调用方法
        T result = (T) method.invoke(target, args);

        //后置增强，日志输出等
        log.info("后置增强：处理日志。。。。。");

        return result;
    }
}
