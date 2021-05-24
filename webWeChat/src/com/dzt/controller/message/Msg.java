package com.dzt.controller.message;

import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.GroupSender;
import com.dzt.bean.selectBean.GroupUser;
import com.dzt.core.BeanFactory;
import com.dzt.entity.GroupMember;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.service.*;
import com.dzt.service.impl.*;
import com.sun.deploy.net.offline.WIExplorerOfflineHandler;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint( value = "/msg",configurator=GetHttpSessionConfigurator.class)
public class Msg {
    private Session session;
    private User user;
    private HttpSession httpSession;
    private GroupMemberService groupMemberService = BeanFactory.getBean(GroupMemberServiceImpl.class);
    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);
    private MessageService messageService = new MessageServiceImpl();
    private PersonRelationshipService prService = BeanFactory.getBean(PersonRelationShipServiceImpl.class);
    private UnreadMsgService unreadMsgService = BeanFactory.getBean(UnreadMsgServiceImpl.class);
    private static ConcurrentHashMap<Integer,Msg> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
          */
     @OnOpen
     public void onOpen(Session session, EndpointConfig config) throws IOException {
         this.session = session;
         HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
         User user = (User) httpSession.getAttribute("user");
         this.httpSession = httpSession;
         this.user = user;
         httpSession.setAttribute("wsSession",session);
         //加入到map中
         webSocketMap.put(user.getId(),this);
         List<Message> messages = unreadMsgService.getUnreadMsg(user.getId());
         for (Message message : messages) {
             if (message.getMsgType() != 2){
                 session.getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
             } else {
                 GroupSender groupSender = messageService.toGroupSender(message);
                 if (groupSender.getVest() == null){
                     continue;
                 }
                 session.getBasicRemote().sendText(MyResult.build().add("msg",groupSender).toJson());
             }
         }
     }

     public void sendMessage(Message msg) throws IOException{

             switch (msg.getMsgType()){
                 case 1:
                     Map map = (Map) httpSession.getAttribute("userAuthorities");
                     int i = (int) map.get("" + msg.getToId());
                     if (i == 1){
                         Message message = new Message(0,this.user.getId(),9,"你已经被好友拉黑或者把好友拉黑，信息发送失败");
                         this.session.getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                     } else if(!prService.isHaveRelationShip(msg.getFromId(),msg.getToId())){
                         Message message = new Message(0,this.user.getId(),9,"你们已经不是好友了！消息发送失败");
                         this.session.getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                     } else {
                         if (webSocketMap.containsKey(msg.getToId())){
                             Msg toWeb = webSocketMap.get(msg.getToId());
                             toWeb.getSession().getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                         }
                         Message m1 = messageService.addMessage(msg);
                         unreadMsgService.add(m1.getId(),m1.getToId());
                     }
                     break;
                 case 3:
                     if (messageService.isHaveSameFriendApplication(msg)){
                         Message message = new Message(0,this.user.getId(),9,"好友申请已经发送过了");
                         this.session.getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                     } else if (prService.isHaveRelationShip(msg.getFromId(),msg.getToId())){
                         Message message = new Message(0,this.user.getId(),9,"你们已经是好友了");
                         this.session.getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                     } else if (msg.getToId() == msg.getFromId()){
                         Message message = new Message(0,this.user.getId(),9,"不能加自己为好友哦");
                         this.session.getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                     } else {
                         Message m3 = messageService.addMessage(msg);
                         if (webSocketMap.containsKey(msg.getToId())){
                             MyResult myResult = MyResult.build().add("msg",m3).add("fromProfile",this.user.getProfile()).add("fromName",this.user.getName());
                             Msg toWeb = webSocketMap.get(msg.getToId());
                             toWeb.session.getBasicRemote().sendText(myResult.toJson());
                         }
                     }
                     break;
                 case 5:
                 case 7:
                     if (webSocketMap.containsKey(msg.getToId())){
                         Msg toWeb = webSocketMap.get(msg.getToId());
                         toWeb.session.getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                     }
                     break;
                 case 9:
                 case 10:
                 case 11:
                 case 12:
                 case 13:
                 case 15:
                     this.session.getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                     break;
                 default:
             }



     }

     @OnClose
     public void onClose(){
         webSocketMap.remove(this.user.getId());
     }


    /**
     * 接受接收客户端消息后的回调
     * @param message
     * @param session
     */
     @OnMessage
     public void onMessage(String message, Session session) throws IOException {


         Message msg = messageService.transform(message);
         switch (msg.getMsgType()){
             case 1:
             case 3:
             case 5:
             case 7:
             case 8:
             case 9:
             case 10:
             case 11:
             case 12:
             case 13:
             case 15:
                 this.sendMessage(msg);
                 break;
             case 2:
             case 4:
             case 14:
                    this.sendGroupMessage(msg);
                 break;
         }


     }

    public Message sendGroupMessage(Message message) throws IOException {
        switch (message.getMsgType()){
            case 2:
                if (!groupMemberService.isHaveMember(message.getToId(),message.getFromId())){
                    Message msg = new Message(0,this.user.getId(),9,"你已经不是群成员了！消息没有被发送过去");
                    this.session.getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                } else {
                    Map map = (Map) httpSession.getAttribute("groupAuthorities");
                    if (0 == (int) map.get("" + message.getToId())){
                        Message msg = new Message(0,this.user.getId(),9,"你已经被禁言了！消息没有被发送过去");
                        this.session.getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                    } else {
                        Message message1 = messageService.addMessage(message);
                        GroupSender groupSender = messageService.toGroupSender(message);
                        List<GroupUser> groupMember = groupMemberService.getGroupMember(groupSender.getToId());
                        for (GroupUser groupUser : groupMember) {
                            Integer userId = groupUser.getUserId();
                            if (webSocketMap.containsKey(userId) && user.getId() != userId){
                                webSocketMap.get(userId).session.getBasicRemote().sendText(MyResult.build().add("msg",groupSender).toJson());
                            }
                            if (userId != user.getId()){
                                unreadMsgService.add(message1.getId(),userId);
                            }
                        }
                    }
                }

                break;
            case 4:
                if (groupMemberService.isHaveMember(message.getToId(),message.getFromId())){
                    Message msg = new Message(0,this.user.getId(),9,"已经是群成员了");
                    this.session.getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                }else if(messageService.isHaveSameGroupApplication(message)){
                    Message msg = new Message(0,this.user.getId(),9,"发生过该请求");
                    this.session.getBasicRemote().sendText(MyResult.build().add("msg",msg).toJson());
                } else {
                    Message m4 = messageService.addMessage(message);
                    List<GroupMember> groupManagements  = groupMemberService.getManagement(message.getToId());
                    for (GroupMember groupManagement : groupManagements) {
                        if (webSocketMap.containsKey(groupManagement.getUserId())){
                            webSocketMap.get(groupManagement.getUserId()).session.getBasicRemote().sendText(MyResult.build().add("msg",m4).toJson());
                        }
                    }
                }
                break;
            case 14:
                Message message14 = messageService.addMessage(message);
                List<GroupUser> groupMember = groupMemberService.getGroupMember(message14.getToId());
                for (GroupUser groupUser : groupMember) {
                    Integer userId = groupUser.getUserId();
                    if (webSocketMap.containsKey(userId) && user.getId() != userId){
                        webSocketMap.get(userId).session.getBasicRemote().sendText(MyResult.build().add("msg",message14).toJson());
                    }
                    if (userId != user.getId()){
                        unreadMsgService.add(message14.getId(),userId);
                    }
                }
                return message14;
        }
        return message;
    }


    public static ConcurrentHashMap<Integer, Msg> getWebSocketMap() {
        return webSocketMap;
    }

    public Session getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public GroupMemberService getGroupMemberService() {
        return groupMemberService;
    }

    public void setGroupMemberService(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public PersonRelationshipService getPrService() {
        return prService;
    }

    public void setPrService(PersonRelationshipService prService) {
        this.prService = prService;
    }

    public UnreadMsgService getUnreadMsgService() {
        return unreadMsgService;
    }

    public void setUnreadMsgService(UnreadMsgService unreadMsgService) {
        this.unreadMsgService = unreadMsgService;
    }

    public static void setWebSocketMap(ConcurrentHashMap<Integer, Msg> webSocketMap) {
        Msg.webSocketMap = webSocketMap;
    }
}
