package com.dzt.bean.page;

import com.dzt.bean.selectBean.GroupSender;
import com.dzt.bean.selectBean.LongCount;
import com.dzt.core.BeanFactory;
import com.dzt.core.DaoFacory;
import com.dzt.dao.MessageDao;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.service.MessageService;
import com.dzt.service.impl.MessageServiceImpl;

import javax.enterprise.inject.spi.Bean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
public class MessagePageBuilder extends PageBulider<MessagePageBuilder>{
    private MessageDao messageDao = DaoFacory.getWebChatDao(MessageDao.class);
    private MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);
    @Override
    MessagePageBuilder buildTotalDataSize(Map map) {
        int type = (int) map.get("type");
        int id = (int) map.get("id");
        //1是要找好友聊天
        //2是要找群的聊天
        if (type == 1){
            User user = (User) map.get("user");
            LongCount longCount = messageDao.getFriendMessageCount(user.getId(), id, id, user.getId()).get(0);
            getPage().setTotalDateSize(longCount.getCount().intValue());
        } else if (type == 2){
            LongCount longCount = messageDao.getGroupMessageCount(id).get(0);
            getPage().setTotalDateSize(longCount.getCount().intValue());
        }
        return this;
    }

    @Override
    MessagePageBuilder buildData(Map map) {
        Page page = getPage();
        int type = (int) map.get("type");
        int id = (int) map.get("id");
        if (type == 1){
            User user = (User) map.get("user");
            List<Message> friendMessage = messageDao.getFriendMessage(user.getId(), id, id, user.getId(), (page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
            page.setData(friendMessage);
        } else if (type == 2){
            List<Message> groupMessage = messageDao.getGroupMessage(id, (page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
            List<GroupSender> groupSenders = new ArrayList<>();
            for (Message message : groupMessage) {
                GroupSender groupSender = messageService.toGroupSender(message);
                groupSenders.add(groupSender);
            }
            getPage().setData(groupSenders);
        }
        return this;
    }
}
