package com.zlk.jdk.dynamicproxy.jdk;

import java.util.List;

/**
 * @Description: 用户服务接口
 * @Author: ZhouLiKuan
 * @Date: 2020/10/29 10:22
 */
public interface UserService {
    List<User> queryUserList(User user);

    User queryUserById(Long id);
}
