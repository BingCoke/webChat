package com.dzt.service;

import com.dzt.bean.selectBean.MyFriend;
import com.dzt.entity.User;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author Z
 */
public interface UserService {
    /**
     * 注册并将注册的用户保存
     * @param user
     * @return
     */
    void register(User user);


    /**
     * 验证是否用户名重复
     * @param username
     * @return
     */
    Boolean hasUsername(String username);

    /**
     * 登录逻辑
     * @param username
     * @param password
     * @return
     */
    List login(String username, String password);

    /**
     * 检查是否邮箱重复
     * @param mail
     * @return
     */
    Boolean checkMail(String mail);

    /**
     * 保存用户的头像
     * @param profile
     * @param username
     * @return
     * @throws IOException
     */
    String saveProfile(Part profile, String username) throws IOException;

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User selectByUsername(String username);

    /**
     * 根据id查找用户
     * @param userId
     * @return
     */
    String selectByUserId(int userId);

    /**
     * 保存修改用户的个人信息
     * @param name
     * @param gender
     * @param phone
     * @param id
     * @return
     */
    String updateUserInformation(String name, String gender, String phone, int id);

    /**
     * 给用户旧邮箱发送验证码去更改邮箱
     * @param userId
     * @return
     */
    String sendMailToUpdate(int userId);

    /**
     * 给旧邮箱发送验证码
     * @param newMail
     * @return
     */
    String sendToNewMailCode(String newMail);

    /**
     * 修改用户的邮箱
     * @param newMail
     * @param userId
     */
    void updateMail(String newMail, int userId);

    /**
     * 修改密码
     * @param password
     * @param userId
     */
    void updatePassword(String password, int userId);


    /**
     * 得到用户的所有好友
     * @param userId
     * @return
     */
    List<MyFriend> getMyFriends(int userId);

    /**
     * 根据id得到用户
     * @param toUserId
     * @return
     */
    User getUserById(Integer toUserId);


    /**
     * 得到自己加入的群的权限
     * @param id
     * @return
     */
    Map getGroupAuthoritiesMap(int id);

    /**
     * 得到自己与朋友之间的权限
     * @param id
     * @return
     */
    Map getFriendAuthoritiesMap(int id);

    /**
     * 保存用户的背景
     * @param file
     * @param user
     * @return
     */
    String savaBack(Part file, User user) throws Exception;

    /**
     * 保存上传过来的文件的接口
     * @param file
     * @return
     */
    String saveFile(Part file) throws IOException;

    /**
     * 封禁用户
     * @param user
     * @param date
     */
    void seal(User user, int date);

    /**
     * 解除某个用户的封禁
     * @param user
     */
    void removeSeal(User user);

    /**
     * 创建一个用户
     * @param user
     * @return
     */
    User add(User user);

}
