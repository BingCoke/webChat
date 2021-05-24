package com.dzt.service;

import com.dzt.entity.Admin;

public interface AdminService {
    /**
     * 管理员登录
     * @param username
     * @param password
     * @return
     */
    Admin login(String username, String password);

    /**
     * 判断用户名是否重复
     * @param username
     * @return
     */
    boolean checkUser(String username);

    /**
     * 增加一个管理
     * @param admin
     */
    void add(Admin admin);
}
