package com.zlk.jdk.thread.general;

import com.zlk.domain.po.User;
import lombok.Data;

import java.util.concurrent.Callable;

/**
 * @author likuan.zhou
 * @title: MyThreadCallable
 * @projectName general-item
 * @description: 实现Callable，重写call()方法，支持返回结果。配合FutureTask使用(需要配合线程池使用)
 * @date 2021/9/10/010 8:37
 */
@Data
public class MyThreadCallable<T> implements Callable<T> {
    private User user;

    /**需要入参可以提供构造方法*/
    public MyThreadCallable(User user) {
        this.user = user;
    }

    public MyThreadCallable() {

    }

    @Override
    public T call() throws Exception {
        // 该处可以返回实际对象
        return (T) ("线程id："+Thread.currentThread().getId()+";线程名称："+Thread.currentThread().getName());
    }
}
