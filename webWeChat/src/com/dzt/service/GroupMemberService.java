package com.dzt.service;

import com.dzt.bean.selectBean.GroupUser;
import com.dzt.entity.GroupMember;

import java.util.List;

/**
 * @author Z
 */
public interface GroupMemberService {
    /**
     * 增加一个群成员
     * @param groupId
     * @param friendId
     * @param power
     */
    void addMember(int groupId, int friendId, int power);

    /**
     * 根据群号找到对应群主的群成员一条信息
     * @param toId
     * @return
     */
    GroupMember getLord(Integer toId);

    /**
     * 找到群的群管理
     * @param toId
     * @return
     */
    List<GroupMember> getManagement(Integer toId);

    /**
     * 检查某个群是否已经有该成员
     * @param groupId
     * @param userId
     * @return
     */
    boolean isHaveMember(Integer groupId, Integer userId);

    /**
     * 得到一个群的群成员
     * @param id
     * @return
     */
    List<GroupUser> getGroupMember(Integer id);

    /**
     * 修改某个群成员的群昵称
     * @param vest
     * @param id
     * @param userId
     */
    void saveVest(String vest, Integer id, Integer userId);

    /**
     * 送某个群成员飞机票
     * @param id
     * @param userId
     */
    void removeMember(Integer id, Integer userId);

    /**
     * 设置群成员的权限
     * @param power
     * @param id
     * @param userId
     */
    void updatePower(int power, Integer id, Integer userId);

    /**
     * 解除某个群成员的禁言
     * @param id
     * @param userId
     */
    void removeMute(Integer id, Integer userId);

    /**
     * 给群成员设置定时禁言
     * @param seconds
     * @param minutes
     * @param hours
     * @param id
     * @param userId
     */
    void mute(int seconds, int minutes, int hours, Integer id, Integer userId);

    /**
     * 得到一个人在群中的群昵称
     *
     * @param userId
     * @param groupId
     * @return
     */
    String getVest(int userId, Integer groupId);

    /**
     * 是否是群管理
     * @return
     * @param userId
     * @param groupId
     */
    boolean isManger(Integer userId,Integer groupId);
}
