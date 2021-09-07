package com.zlk.jdk.dynamicproxy.cglib;

import lombok.Data;

/**
 * @Description: 用户实体
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 10:52
 */
@Data
public class User {
    private Long id;

    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
