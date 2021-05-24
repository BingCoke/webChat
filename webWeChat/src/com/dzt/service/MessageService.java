package com.dzt.service;

import com.dzt.bean.selectBean.GroupSender;
import com.dzt.bean.selectBean.Sender;
import com.dzt.entity.Message;

import java.util.List;

/**
 * @author Z
 */

public interface MessageService {


    /**
     *  增添一条信息
     * @param msg
     */
    Message addMessage(Message msg);

    /**
     * 验证是否有相同的好友申请发出
     * @return
     * @param msg
     */
    boolean isHaveSameFriendApplication(Message msg);

    void removeById(int msgId);

    /**
     * 得到关于群申请的消息
     * @param id
     * @return
     */
    String getGroupApplicationMsg(int id);


    /**
     * 得到好友的申请消息
     * @param id
     * @return
     */
    String getFriendApplicationMsg(int id);


    /**
     * 验证是否有相同的群申请
     * @param message
     * @return
     */
    boolean isHaveSameGroupApplication(Message message);

    /**
     * 转化msg
     * @param message
     * @return
     */
    Message transform(String message);

    /**
     * 将一条群消息包装
     * @param message
     * @return
     */
    GroupSender toGroupSender(Message message);

    /**
     * 得到群公告
     * @param id
     * @return
     */
    Message getGroupAnnouncement(Integer id);

    /**
     * 删除一个信息
     * @param message
     */
    void remove(Message message);

    /**
     * 得到举报的消息
     * @return
     */
    List<Message> getReport();

}
