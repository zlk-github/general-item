package com.zlk.jdk.dynamicproxy.cglib;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 代理对象：用户业务实现类；cglib动态代理不需要实现统一的接口
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 10:27
 */
public class UserService {
    public List<User> queryUserList(User user) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1001L,"用户1001"));
        userList.add(new User(1002L,"用户1002"));
        return userList;
    }

    public User queryUserById(Long id) {
        return new User(id,"用户:"+id);
    }
}
