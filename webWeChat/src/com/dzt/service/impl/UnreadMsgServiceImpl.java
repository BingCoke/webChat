package com.dzt.service.impl;

import com.dzt.core.DaoFacory;
import com.dzt.dao.UnreadMsgDao;
import com.dzt.entity.Message;
import com.dzt.entity.UnreadMsg;
import com.dzt.service.UnreadMsgService;

import java.util.List;

/**
 * @author Z
 */
public class UnreadMsgServiceImpl implements UnreadMsgService {
    private UnreadMsgDao unreadMsgDao = DaoFacory.getWebChatDao(UnreadMsgDao.class);
    @Override
    public void add(int msgId,int userId) {
        int i = 0;
        UnreadMsg unreadMsg = new UnreadMsg(msgId,userId);
        unreadMsgDao.add(unreadMsg);
    }

    @Override
    public List<Message> getUnreadMsg(int id) {
        return unreadMsgDao.getUnreadMsg(id);
    }

    @Override
    public void deleteMyFriendMsg(int userId, int friendId) {
        unreadMsgDao.deleteMyFriendMsg(friendId,userId,userId);
    }

    @Override
    public void deleteMyGroupMsg(int groupId, int userId) {
        int i = 0;
        unreadMsgDao.deleteMyGroupMsg(groupId,userId);
    }

    @Override
    public void deleteAnnouncement(Integer msgId) {
        unreadMsgDao.deleteAnnouncement(msgId);
    }

    @Override
    public void deleteMyAnnouncement(Integer groupId, Integer userId) {
        unreadMsgDao.deleteMyAnnouncement(groupId,userId);
    }


}
