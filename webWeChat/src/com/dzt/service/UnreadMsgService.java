package com.dzt.service;

import com.dzt.entity.Message;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * @author Z
 */
public interface UnreadMsgService {
    /**
     * 增加一个未阅读信息
     * @param msgId
     * @param userId
     */
    void add(int msgId,int userId);


    /**
     * 得到用户没有阅读过的信息
     * @param id
     * @return
     */
    List<Message> getUnreadMsg(int id);


    /**
     * 删除好友发给我的未读消息
     * @param userId
     * @param friendId
     */
    void deleteMyFriendMsg(int userId,int friendId);

    /**
     * 删除群未读消息
     * @param groupId
     * @param userId
     */
    void deleteMyGroupMsg(int groupId,int userId);

    /**
     * 将公告的未读全部删除
     * @param msgId
     */
    void deleteAnnouncement(Integer msgId);

    /**
     * 删除用户的未读群公告
     * @param groupId
     * @param userId
     */
    void deleteMyAnnouncement(Integer groupId, Integer userId);
}
