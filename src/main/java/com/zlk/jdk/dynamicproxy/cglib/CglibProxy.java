package com.zlk.jdk.dynamicproxy.cglib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;


import java.lang.reflect.Method;

/**
 * @Description: cglib方式实现动态代理器-需要实现MethodInterceptor 接口，重写invoke方法。
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 10:46
 */
@Slf4j
public class CglibProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // methodProxy 为方法代理
        // method 当前代理对象执行的具体方法
        // objects 当前代理对象执行的具体方法的入参
        // o 被代理对象(service)

        log.info("方法名称："+method.getName());

        //前置处理，条件处理、字段校验、安全等
        if( objects != null &&objects[0] instanceof User){
            log.info("前置处理：条件处理、字段校验、安全等");
        }

        //调用方法
        Object result = methodProxy.invokeSuper(o, objects);
        //后置增强，日志输出等
        log.info("后置增强：处理日志。。。。。");

        return result;
    }

}
