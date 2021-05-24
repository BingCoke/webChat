package com.dzt.dao;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.bean.selectBean.Sender;
import com.dzt.core.BaseDao;
import com.dzt.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
public interface MessageDao extends BaseDao<Message> {
    /**
     * 找到相同的好友申请
     * @param from
     * @param to
     * @return
     */
    List<LongCount> findSameFriendApplication(Integer from, Integer to);


    /**
     * 找到用户的好友消息
     * @param id
     * @return
     */
    List<Sender> getFriendApplicationMsg(int id);

    /**
     * 找到该用户的管理群的群申请消息
     * @param id
     * @return
     */
    List<Sender> getGroupApplicationMsg(int id);

    /**
     * 查找是否有相同的群申请
     * @param fromId
     * @param toId
     * @return
     */
    List<LongCount> findSameGroupApplication(Integer fromId, Integer toId);



    /**
     * 得到群公告
     *
     * @param id
     * @return
     */
    List<Message> getGroupAnnouncement(Integer id);

    /**
     * 得到举报的消息
     * @return
     */
    List<Message> getReport();

    /**
     * 得到用户和某个好友的历史聊天记录的数量
     * @return
     * @param myId
     * @param friendId
     * @param friendId1
     * @param myId1
     */
    List<LongCount> getFriendMessageCount(int myId,int friendId,int friendId1,int myId1);

    /**
     * 得到某个好友的历史聊天记录
     * @param myId
     * @param friendId
     * @param friendId1
     * @param myId1
     * @param start
     * @param end
     * @return
     */
    List<Message> getFriendMessage(int myId,int friendId,int friendId1,int myId1,int start,int end);

    /**
     * 得到群聊的历史消息
     * @return
     */
    List<LongCount> getGroupMessageCount(int id);

    /**
     * 得到某个群的历史消息记录
     * @param id
     * @param start
     * @param end
     * @return
     */
    List<Message> getGroupMessage(int id,int start,int end);
}
