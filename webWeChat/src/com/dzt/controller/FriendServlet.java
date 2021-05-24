package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.MyFriend;
import com.dzt.bean.selectBean.Sender;
import com.dzt.controller.message.Msg;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.Message;
import com.dzt.entity.PersonRelationship;
import com.dzt.entity.User;
import com.dzt.service.MessageService;
import com.dzt.service.PersonRelationshipService;
import com.dzt.service.UnreadMsgService;
import com.dzt.service.UserService;
import com.dzt.service.impl.MessageServiceImpl;
import com.dzt.service.impl.PersonRelationShipServiceImpl;
import com.dzt.service.impl.UnreadMsgServiceImpl;
import com.dzt.service.impl.UserServiceImpl;
import com.dzt.util.ToObjectUtil;

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

@WebServlet("/friend/*")
public class FriendServlet extends MyServlet {
    private PersonRelationshipService prService = BeanFactory.getBean(PersonRelationShipServiceImpl.class);
    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);
    private PersonRelationshipService personRelationshipService = BeanFactory.getBean(PersonRelationShipServiceImpl.class);
    private MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);
    private UnreadMsgService unreadMsgService = BeanFactory.getBean(UnreadMsgServiceImpl.class);
    @Override
    public boolean filter(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getPower() == 0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 得到用户好友
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getMyFriends(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<MyFriend> myFriends = userService.getMyFriends(user.getId());
        PrintWriter writer = resp.getWriter();
        writer.write(MyResult.build().setCode(200).setData(myFriends).toJson());
    }


    /**
     * 查看好友信息暂存一下数据
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void toLook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MyFriend myFriend = ToObjectUtil.getObject(req,MyFriend.class);
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        session.setAttribute("myFriendToLook",myFriend);
        writer.write(MyResult.build().setData(myFriend).setCode(200).toJson());
    }

    /**
     * 看朋友圈时的页面跳转
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toLookDo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/user/lookFriend.html").forward(req,resp);
    }

    /**
     * 得到该好友的相关信息
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        MyFriend myFriend = (MyFriend) session.getAttribute("myFriendToLook");
        User user = (User) session.getAttribute("user");
        boolean isBlack = prService.isBlack(user.getId(),myFriend.getUserId());
        MyResult myResult = MyResult.build().setCode(200).add("myFriend",myFriend);
        if (isBlack){
            myResult.add("black",1);
        } else {
            myResult.add("black",0);
        }
        PrintWriter writer = resp.getWriter();
        writer.write(myResult.toJson());
    }

    /**
     * 对于该好友的拉黑
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void black(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        String black = req.getParameter("black");
        MyFriend myFriend = (MyFriend) session.getAttribute("myFriendToLook");
        if (black.equals("1")){
            prService.updateBlack(myFriend.getId(),0);
            writer.write(MyResult.build().setMsg("已经将好友取消拉黑").toJson());
        } else {
            prService.updateBlack(myFriend.getId(),1);
            writer.write(MyResult.build().setMsg("已经将好友拉黑").toJson());
        }
        Message message = new Message();
        message.setMsgType(11);
        ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
        Msg msg1 = webSocketMap.get(user.getId());
        msg1.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
        if (webSocketMap.containsKey(myFriend.getUserId())){
            Msg msg = webSocketMap.get(myFriend.getUserId());

            msg.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
            Map map1 = userService.getFriendAuthoritiesMap(user.getId());
            req.getSession().setAttribute("userAuthorities",map1);
        }
    }


    /**
     * 好友的备注更新
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updateRemark(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String remark = req.getParameter("remark");
        MyFriend myFriend = (MyFriend) session.getAttribute("myFriendToLook");
        personRelationshipService.updateRemark(remark,myFriend.getId());
        PrintWriter writer = resp.getWriter();
        writer.write(MyResult.build().setCode(200).setMsg("修改成功").toJson());
    }


    /**
     * 好友删除
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        MyFriend myFriend = (MyFriend) session.getAttribute("myFriendToLook");
        User user = (User) req.getSession().getAttribute("user");
        prService.deleteFriend(user.getId(),myFriend.getUserId());
        PrintWriter writer = resp.getWriter();
        writer.write(MyResult.build().setCode(200).setMsg("删除好友成功").toJson());
    }

    /**
     * 得到好友申请的消息
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getFriendApplication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        String senders = messageService.getFriendApplicationMsg(user.getId());
        resp.getWriter().write(senders);
    }


    /**
     * 通过好友申请
     */
    @ToJSON
    public void passApplication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Sender sender = ToObjectUtil.getObject(req,Sender.class);
        messageService.removeById(sender.getMsgId());
        User user = (User) session.getAttribute("user");
        personRelationshipService.add(new PersonRelationship(sender.getUserId(),user.getId(),user.getName(),0));
        personRelationshipService.add(new PersonRelationship(user.getId(),sender.getUserId(),sender.getName(),0));
        PrintWriter writer = resp.getWriter();
        writer.write(MyResult.build().setCode(200).setMsg("增加好友成功").toJson());
    }


    /**
     * 拒绝好友申请
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void refuseApplication(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        Sender sender = ToObjectUtil.getObject(req,Sender.class);
        messageService.removeById(sender.getMsgId());
        writer.write(MyResult.build().setCode(200).setMsg("已经拒绝").toJson());
    }


    /**
     * 删除好友申请的信息
     * @param req
     * @param resp
     */
    public void deleteMyFriendMsg(HttpServletRequest req, HttpServletResponse resp){
        int friendId = Integer.parseInt(req.getParameter("friendId"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        unreadMsgService.deleteMyFriendMsg(user.getId(),friendId);
    }
}
