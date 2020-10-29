### 1 practice 普通练习项目

##### 1.1 dynamicproxy包 动态代理

    JDK原生动态代理：是由java内部的反射机制来实现的，需要目标类（被代理对象）实现统一的接口。
    
        Proxy：调用静态newProxyInstance()方法，用于创建被代理对象的实例。
        
        InvocationHandler（被代理对象构造方法注入）:每个动态代理都关联一个InvocationHandler，被代理对象实例（Proxy.newProxyInstance()）调用具体方法时，
                                                 会转到InvocationHandler下的invoke()方法。invoke方法可以反射调用具体方法，
                                                 并可以做前置处理（安全、权限、参数校验、条件更改-如sql）与后置增强(如操作完成后的日志输出)。
    
    CGLIB动态代理：借助asm来实现;可以通过将asm生成的类进行缓存，解决asm生成类过程低效问题。不需要目标类（被代理对象）实现统一的接口。但是需要单独引入jar包

        Enhancer：指定代理对象与被代理对象，调用create()创建被被代理对象实例。
    
        MethodInterceptor：被代理对象调用方法时，转到intercept方法下。 
                           并可以做前置处理（安全、权限、参数校验、条件更改-如sql）与后置增强(如操作完成后的日志输出)。
