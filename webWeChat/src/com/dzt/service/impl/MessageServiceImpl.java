package com.dzt.service.impl;

import com.alibaba.fastjson.JSON;
import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.GroupSender;
import com.dzt.bean.selectBean.Sender;
import com.dzt.core.BeanFactory;
import com.dzt.core.DaoFacory;
import com.dzt.dao.MessageDao;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.service.GroupMemberService;
import com.dzt.service.MessageService;
import com.dzt.service.UserService;
import org.omg.CORBA.UserException;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author Z
 */
public class MessageServiceImpl implements MessageService {
    private final MessageDao messageDao = DaoFacory.getWebChatDao(MessageDao.class);
    private final UserService userService = BeanFactory.getBean(UserServiceImpl.class);
    public static final Object monitor = new Object();
    @Override
    public Message addMessage(Message msg) {
          return  messageDao.add(msg);
    }

    @Override
    public boolean isHaveSameFriendApplication(Message msg) {
        long count =  messageDao.findSameFriendApplication(msg.getFromId(), msg.getToId()).get(0).getCount();
        if (count == 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void removeById(int msgId) {
        messageDao.remove(msgId);
    }

    @Override
    public String getGroupApplicationMsg(int id) {
        List<Sender> senders = messageDao.getGroupApplicationMsg(id);
        return MyResult.build().setData(200).setData(senders).toJson();
    }

    @Override
    public String getFriendApplicationMsg(int id) {
        List<Sender> senders = messageDao.getFriendApplicationMsg(id);
        return MyResult.build().setData(200).setData(senders).toJson();
    }

    @Override
    public boolean isHaveSameGroupApplication(Message message) {
        long count =  messageDao.findSameGroupApplication(message.getFromId(), message.getToId()).get(0).getCount();
        if (count == 0){
            return false;
        } else {
            return true;
        }
    }


    @Override
    public  Message transform(String message){
                Message msg = JSON.parseObject(message, Message.class);
                msg.setId(0);
                Date date = new Date();
                msg.setTime(new Timestamp(date.getTime()));
                return msg;
    }

    @Override
    public GroupSender toGroupSender(Message message) {
        GroupSender groupSender = new GroupSender();
        User user = userService.getUserById(message.getFromId());
        GroupMemberService groupMemberService = new GroupMemberServiceImpl();
        String vest = groupMemberService.getVest(message.getFromId(), message.getToId());
        if (vest == null || vest == ""){
            vest = user.getName();
        }
        groupSender.setVest(vest);
        groupSender.setProfile(user.getProfile());
        groupSender.setContent(message.getContent());
        groupSender.setMsgType(message.getMsgType());
        groupSender.setToId(message.getToId());
        groupSender.setFromId(user.getId());
        groupSender.setStatus(message.getStatus());
        groupSender.setFileName(message.getFileName());
        return groupSender;
    }

    @Override
    public Message getGroupAnnouncement(Integer id) {
        List<Message> messages = messageDao.getGroupAnnouncement(id);
        if (messages.size() > 0){
            return messages.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void remove(Message message) {
        messageDao.remove(message.getId());
    }

    @Override
    public List<Message> getReport() {
        return messageDao.getReport();
    }

}
