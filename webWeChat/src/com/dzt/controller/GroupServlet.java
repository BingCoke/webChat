package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.bean.page.Page;
import com.dzt.bean.page.PageDirector;
import com.dzt.bean.selectBean.GroupUser;
import com.dzt.bean.selectBean.Sender;
import com.dzt.controller.message.Msg;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.GroupMember;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.entity.UserGroup;
import com.dzt.service.*;
import com.dzt.service.impl.*;
import com.dzt.util.StringUtil;
import com.dzt.util.ToObjectUtil;
import org.junit.Test;


import javax.enterprise.inject.spi.Bean;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Z
 */
@WebServlet("/group/*")
public class GroupServlet extends MyServlet {
    private UserGroupService userGroupService = BeanFactory.getBean(UserGroupServiceImpl.class);
    private GroupMemberService groupMemberService = BeanFactory.getBean(GroupMemberServiceImpl.class);
    private MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);
    private UnreadMsgService unreadMsgService = BeanFactory.getBean(UnreadMsgServiceImpl.class);

    @Override
    public boolean filter(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getPower() == 0 && user.getPower() == 2){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 分页查找群
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void findGroups(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageDirector pageDirector = ToObjectUtil.getObject(req,PageDirector.class);
        String name = req.getParameter("name");
        String id = req.getParameter("id");
        PrintWriter writer = resp.getWriter();
        if (StringUtil.notEmpty(id)){
            pageDirector.getSelect().put("id",Integer.parseInt(id));
        } else if (StringUtil.notEmpty(name)){
            pageDirector.getSelect().put("name",name);
        }

        if (StringUtil.notEmpty(id) || StringUtil.notEmpty(name)){
            Page<UserGroup> groupPage = pageDirector.BuildGroupPage();
            if (groupPage.getTotalDateSize() == 0){
                writer.write(MyResult.build().setCode(2002).setMsg("得到数据为空").toJson());
            } else {
                writer.write(MyResult.build().setCode(200).setMsg("成功！").add("data",groupPage).toJson());
            }
        }  else {
            writer.write(MyResult.build().setCode(2001).setMsg("输入数据为空").toJson());
        }
    }

    /**
     * 打开一个群创建的页面
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toCreat(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/creatGroup.html").forward(req,resp);
    }


    @ToJSON
    public void creat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        String[] friends = parameterMap.get("friend");
        int groupId = userGroupService.addGroup(new UserGroup(req.getParameter("groupName")));
        if (friends != null){
            for (String friend : friends) {
                int friendId = Integer.parseInt(friend);
                groupMemberService.addMember(groupId,friendId,1);
            }
        }
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        groupMemberService.addMember(groupId,user.getId(),3);
        resp.getWriter().write(MyResult.build().setMsg("创建成功").toJson());
        Message message = new Message(0,0,13,"");
        ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
        webSocketMap.get(user.getId()).getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
        if (friends != null){
            for (String friend : friends) {
                int friendId = Integer.parseInt(friend);
                if (webSocketMap.containsKey(friendId)){
                    webSocketMap.get(friendId).getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                }
            }
        }
    }

    /**
     * 得到群申请的消息
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getGroupApplication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        String senders = messageService.getGroupApplicationMsg(id);
        resp.getWriter().write(senders);
    }

    /**
     * 拒绝群申请
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void refuseApplication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Sender sender = ToObjectUtil.getObject(req,Sender.class);
        messageService.removeById(sender.getMsgId());
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("已拒绝").toJson());
    }

    /**
     * 通过群申请
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void passApplication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Sender sender = ToObjectUtil.getObject(req,Sender.class);
        User user = (User) req.getSession().getAttribute("user");
        if (!groupMemberService.isManger(user.getId(),sender.getToId())){
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }else if (groupMemberService.isHaveMember(sender.getToId(),sender.getUserId())){
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("该成员已经加入了群聊").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("已通过").toJson());
            groupMemberService.addMember(sender.getToId(),sender.getUserId(),1);
            messageService.removeById(sender.getMsgId());
            ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
            if (webSocketMap.containsKey(sender.getUserId())){
                Message message = new Message(0,0,13,"");
                webSocketMap.get(sender.getUserId()).getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                Message message1 = messageService.getGroupAnnouncement(sender.getToId());
                unreadMsgService.add(message1.getId(),sender.getUserId());
                webSocketMap.get(sender.getUserId()).getSession().getBasicRemote().sendText(MyResult.build().add("msg",message1).toJson());
            }
        }
    }

    /**
     * 得到我的群
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getMyGroups(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String r = userGroupService.getMyGroups(user.getId());
        resp.getWriter().write(r);
    }


    /**
     * 得到用户的群权限
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getAuthorities(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        UserService userService = BeanFactory.getBean(UserServiceImpl.class);
        Map map = userService.getGroupAuthoritiesMap(user.getId());
        req.getSession().setAttribute("groupAuthorities",map);
        resp.getWriter().write(MyResult.build().setCode(200).setData(map).toJson());
    }


    /**、
     * 查看某个群的信息
     * @param req
     * @param resp
     */
    public void toLookGroup(HttpServletRequest req, HttpServletResponse resp){
        UserGroup userGroup = ToObjectUtil.getObject(req,UserGroup.class);
        req.getSession().setAttribute("groupToLook",userGroupService.selectById(userGroup.getId()));
    }

    /**
     * 页面跳转到群查看
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void lookGroup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/lookGroup.html").forward(req,resp);
    }

    /**
     * 得到群的公告
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getAnnouncement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        Message message = messageService.getGroupAnnouncement(groupToLook.getId());
        if (message == null){
            message = new Message();
            message.setId(0);
            message.setContent("");
        }
        req.getSession().setAttribute("announcement",message);
        resp.getWriter().write(MyResult.build().setData(message.getContent()).setCode(200).toJson());
    }


    /**
     * 删除群公告
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void removeAnnouncement(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = (User) req.getSession().getAttribute("user");
        String announcement = req.getParameter("announcement");
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        Message message = (Message) req.getSession().getAttribute("announcement");
        unreadMsgService.deleteAnnouncement(message.getId());
        messageService.remove(message);
        resp.getWriter().write(MyResult.build().setMsg("更改成功").toJson());
        Message message1 = new Message(user.getId(),groupToLook.getId(),14,announcement);
        Message message2 = Msg.getWebSocketMap().get(user.getId()).sendGroupMessage(message1);
        req.getSession().setAttribute("announcement",message2);
    }

    /**
     * 得到要看的群的信息
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getLookGroupInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        List<GroupUser> groupUsers = groupMemberService.getGroupMember(groupToLook.getId());
        resp.getWriter().write(MyResult.build().add("group",groupToLook).add("groupMember",groupUsers).toJson());
    }

    /**
     * 查看某个群成员的信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void groupMemberInformation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GroupUser groupUser = ToObjectUtil.getObject(req,GroupUser.class);
        req.getSession().setAttribute("groupUserToLook",groupUser);
    }

    /**
     * 跳转页面
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toUpdateMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/groupMemberInformation.html").forward(req,resp);
    }

    /**
     * 将信息传入
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getGroupMemberInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GroupUser groupUser = (GroupUser) req.getSession().getAttribute("groupUserToLook");
        resp.getWriter().write(MyResult.build().setData(groupUser).toJson());
    }

    /**
     * 更新某个群成员的群昵称
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updateVest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GroupUser groupUser = ToObjectUtil.getObject(req,GroupUser.class);
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        Map map = (Map) req.getSession().getAttribute("groupAuthorities");
        int power = (int) map.get(groupToLook.getId());
        if (power == 2 || power == 3){
            groupMemberService.saveVest(groupUser.getVest(),groupToLook.getId(),groupUser.getUserId());
            resp.getWriter().write(MyResult.build().setMsg("修改成功").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }

    }

    /**
     * 删除某个群成员
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void deleteMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GroupUser groupUser = ToObjectUtil.getObject(req,GroupUser.class);
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        Map map = (Map) req.getSession().getAttribute("groupAuthorities");
        int power = (int) map.get(groupToLook.getId());
        if (power == 2 || power == 3){
            groupMemberService.removeMember(groupToLook.getId(),groupUser.getUserId());
            resp.getWriter().write(MyResult.build().setMsg("已经将该成员踢出").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }


    }

    /**
     * 把某个群成员提升为管理或者群成员
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updatePower(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        GroupUser groupUser = ToObjectUtil.getObject(req,GroupUser.class);
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");

        Map map = (Map) req.getSession().getAttribute("groupAuthorities");
        int power = (int) map.get(groupToLook.getId());
        if (power == 2 || power == 3){
            if (groupUser.getPower() == 1){
                groupMemberService.updatePower(2,groupToLook.getId(),groupUser.getUserId());
                resp.getWriter().write(MyResult.build().setMsg("已经将其变成管理").toJson());
            } else if (groupUser.getPower() == 2){
                groupMemberService.updatePower(1,groupToLook.getId(),groupUser.getUserId());
                resp.getWriter().write(MyResult.build().setMsg("已经把他撤下").toJson());
            }
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }

    }


    /**
     * 禁言删除
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void removeMute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map map = (Map) req.getSession().getAttribute("groupAuthorities");

        GroupUser groupUser = (GroupUser) req.getSession().getAttribute("groupUserToLook");
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        int power = (int) map.get(groupToLook.getId());
        if (power == 2 || power == 3){
            groupMemberService.removeMute(groupToLook.getId(),groupUser.getUserId());
            ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
            if (webSocketMap.containsKey(groupUser.getUserId())){
                Msg msg = webSocketMap.get(groupUser.getUserId());
                Message message = new Message(0,groupToLook.getId(),10,"");
                msg.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
            }
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }

    }


    /**
     * 禁言群成员
     * @param req
     * @param resp
     * @throws IOException
     */
    public void mute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int hours = Integer.parseInt(req.getParameter("hours"));
        int minutes = Integer.parseInt(req.getParameter("minutes"));
        int seconds = Integer.parseInt(req.getParameter("seconds"));
        GroupUser groupUser = (GroupUser) req.getSession().getAttribute("groupUserToLook");
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        Map map = (Map) req.getSession().getAttribute("groupAuthorities");
        int power = (int) map.get(groupToLook.getId());
        if (power == 2 || power == 3){
            groupMemberService.mute(seconds,minutes,hours,groupToLook.getId(),groupUser.getUserId());
            ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
            if (webSocketMap.containsKey(groupUser.getUserId())){
                Msg msg = webSocketMap.get(groupUser.getUserId());
                Message message = new Message(0,groupToLook.getId(),10,"");
                msg.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
            }
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }


    }

    /**
     * 更改群名字
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updateName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        Map map = (Map) req.getSession().getAttribute("groupAuthorities");
        int power = (int) map.get(groupToLook.getId());
        if (power == 2 || power == 3){
            groupToLook.setName(name);
            userGroupService.save(groupToLook);
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("修改成功").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限").toJson());
        }

    }

    /**
     * 查看自己的群昵称前先储存看那个群的
     * @param req
     * @param resp
     */
    public void toLookGroupVest(HttpServletRequest req, HttpServletResponse resp){
        UserGroup userGroup = ToObjectUtil.getObject(req,UserGroup.class);
        req.getSession().setAttribute("groupVestToLook",userGroup);
    }

    /**
     * 看群昵称页面的跳转
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void lookGroupVest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/group/updateGroupVest.html").forward(req,resp);
    }

    /**
     * 得到我的群昵称
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getMyVest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserGroup userGroup = (UserGroup) req.getSession().getAttribute("groupVestToLook");
        User user = (User) req.getSession().getAttribute("user");
        String vest = groupMemberService.getVest(user.getId(),userGroup.getId());
        resp.getWriter().write(MyResult.build().setCode(200).setData(vest).toJson());
    }

    /**
     * 修改我的群昵称
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updateMyVest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String vest = req.getParameter("vest");
        UserGroup userGroup = (UserGroup) req.getSession().getAttribute("groupVestToLook");
        User user = (User) req.getSession().getAttribute("user");
        groupMemberService.saveVest(vest,userGroup.getId(),user.getId());
        resp.getWriter().write(MyResult.build().setData(200).setMsg("修改成功").toJson());
    }

    /**
     * 删除群的申请消息
     * @param req
     * @param resp
     */
    public void deleteMyGroupMsg(HttpServletRequest req, HttpServletResponse resp){
        int groupId = Integer.parseInt(req.getParameter("groupId")) ;
        User user = (User) req.getSession().getAttribute("user");
        unreadMsgService.deleteMyGroupMsg(groupId,user.getId());
    }

    /**
     * 删除已读群公告
     * @param req
     * @param resp
     */
    public void deleteMyAnnouncement(HttpServletRequest req, HttpServletResponse resp){
        int groupId = Integer.parseInt(req.getParameter("groupId"));
        User user = (User) req.getSession().getAttribute("user");
        unreadMsgService.deleteMyAnnouncement(groupId,user.getId());
    }

    /**
     * @所有人
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void atAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserGroup groupToLook = (UserGroup) req.getSession().getAttribute("groupToLook");
        GroupMember lord = groupMemberService.getLord(groupToLook.getId());
        User user = (User) req.getSession().getAttribute("user");
        if (lord.getUserId().equals(user.getId())){
            List<GroupUser> groupMember = groupMemberService.getGroupMember(groupToLook.getId());
            for (GroupUser groupUser : groupMember) {
                if (groupUser.getUserId().equals(user.getId())){
                    continue;
                }
                ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
                if (webSocketMap.containsKey(groupUser.getUserId())) {
                    Msg msg = webSocketMap.get(groupUser.getUserId());
                    Message message = new Message(user.getId(),groupUser.getUserId(),7,
                            user.getName() +"在群"+ groupToLook.getName() +"@了一下你");
                    msg.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                }
            }
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("ok").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("不是群主没办法@所有人啊").toJson());
        }
    }
}
