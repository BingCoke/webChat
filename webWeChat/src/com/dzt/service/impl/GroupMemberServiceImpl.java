package com.dzt.service.impl;

import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.GroupUser;
import com.dzt.controller.message.Msg;
import com.dzt.core.DaoFacory;
import com.dzt.dao.GroupMemberDao;
import com.dzt.dao.UserDao;
import com.dzt.entity.GroupMember;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.service.GroupMemberService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Z
 */
public class GroupMemberServiceImpl implements GroupMemberService {
    private GroupMemberDao groupMemberDao = DaoFacory.getWebChatDao(GroupMemberDao.class);
    private UserDao userDao = DaoFacory.getWebChatDao(UserDao.class);
    @Override
    public void addMember(int groupId, int friendId, int power) {
        User user = userDao.selectById(friendId);
        groupMemberDao.add(new GroupMember(groupId,friendId,power,user.getName()));
    }

    @Override
    public GroupMember getLord(Integer toId) {
        return groupMemberDao.selectGroupLord(toId).get(0);
    }

    @Override
    public List<GroupMember> getManagement(Integer toId) {
        return groupMemberDao.getManagement(toId);
    }

    @Override
    public boolean isHaveMember(Integer groupId, Integer userId) {
        List<GroupMember> groupMembers = groupMemberDao.getMember(groupId,userId);
        if (groupMembers.size() > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<GroupUser> getGroupMember(Integer id) {
        return groupMemberDao.getGroupMember(id);
    }

    @Override
    public void saveVest(String vest, Integer id, Integer userId) {
        groupMemberDao.saveVest(vest,id,userId);
    }

    @Override
    public void removeMember(Integer id, Integer userId) {
        groupMemberDao.deleteMember(id,userId);
    }

    @Override
    public void updatePower(int power, Integer id, Integer userId) {
        groupMemberDao.updatePower(power,id,userId);
    }

    @Override
    public void removeMute(Integer id, Integer userId) {
        groupMemberDao.updatePower(1,id,userId);
    }

    @Override
    public void mute(int seconds, int minutes, int hours, Integer id, Integer userId) {

        groupMemberDao.updatePower(0,id,userId);
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                groupMemberDao.updatePower(1,id,userId);
                ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
                if (webSocketMap.containsKey(userId)){
                    Msg msg = webSocketMap.get(userId);
                    Message message = new Message(0,userId,10,"");
                    try {
                        msg.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };
        timer.schedule(runnable,seconds + minutes*60 + 60*60*hours, TimeUnit.SECONDS);
    }

    @Override
    public String getVest(int userId, Integer groupId) {
        List<GroupMember> members = groupMemberDao.getMember( groupId, userId);
        if (members.size() == 0){
            return null;
        } else  {
            return members.get(0).getVest();
        }
    }

    @Override
    public boolean isManger(Integer userId, Integer groupId) {
        List<GroupUser> groupMember = getGroupMember(groupId);
        for (GroupUser groupUser : groupMember) {
            if (groupUser.getUserId().equals(userId)){
                return true;
            }
        }

        return false;
    }
}
