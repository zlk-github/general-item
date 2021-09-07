package com.zlk.po;

import lombok.Data;

/**
 * @author likuan.zhou
 * @title: User
 * @projectName practice
 * @description: 用户类
 * @date 2021/9/2/002 10:43
 */
@Data
public class User {
    /**id*/
    private Long id;
    /**名字*/
    private String name;
    /**年龄*/
    private Integer sex;
}
