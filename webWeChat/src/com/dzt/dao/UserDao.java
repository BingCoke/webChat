package com.dzt.dao;

import com.dzt.bean.selectBean.MyFriend;
import com.dzt.core.BaseDao;
import com.dzt.entity.User;

import java.util.List;

public interface UserDao extends BaseDao<User> {
    /**
     * 更改用户的头像的地址
     * @param filePath
     * @param username
     */
    void saveProfile(String filePath, String username);

    /**
     * 修改用户的个人信息
     * @param name
     * @param gender
     * @param phone
     * @param id
     */
    void updateInformation(String name, String gender, String phone, int id);

    /**
     * 修改用户的邮箱
     * @param newMail
     * @param userId
     */
    void updateMail(String newMail, int userId);

    /**
     * 修改用户的密码
     * @param password
     * @param userId
     */
    void updatePassword(String password, int userId);

    /**
     * 得到用户的所有的好友
     * @param userId
     * @return
     */
    List<MyFriend> getMyFriend(int userId);

    /**
     * 设置用户的聊天背景
     * @param s
     * @param id
     */
    void saveBack(String s, int id);

    /**
     * 更新用户的权限
     * @param i
     * @param id
     */
    void updatePower(int i, int id);
}
