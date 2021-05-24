package com.dzt.dao;

import com.dzt.core.BaseDao;
import com.dzt.entity.Message;
import com.dzt.entity.UnreadMsg;

import java.util.List;

/**
 * @author Z
 */
public interface UnreadMsgDao extends BaseDao<UnreadMsg> {
    /**
     * 得到用户没有阅读的msg
     * @param id
     * @return
     */
    List<Message> getUnreadMsg(int id);

    /**
     * 删除群的未读消息
     * @param groupId
     * @param userId
     */
    void deleteMyGroupMsg(int groupId, int userId);

    /**
     * 删除某个好友的未读消息
     * @param fromId
     * @param toId
     * @param userId
     */
    void deleteMyFriendMsg(int fromId, int toId, int userId);

    /**
     * 将群公告的未读全部删除
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
