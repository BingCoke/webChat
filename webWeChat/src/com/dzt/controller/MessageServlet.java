package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.bean.page.Page;
import com.dzt.bean.page.PageDirector;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.service.MessageService;
import com.dzt.service.PersonRelationshipService;
import com.dzt.service.UserService;
import com.dzt.service.impl.MessageServiceImpl;
import com.dzt.service.impl.PersonRelationShipServiceImpl;
import com.dzt.service.impl.UserServiceImpl;
import com.dzt.util.ToObjectUtil;
import sun.misc.resources.Messages;

import javax.enterprise.inject.spi.Bean;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Z
 */
@WebServlet("/message/*")
public class MessageServlet extends MyServlet{
    private MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);
    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);


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
     * 看朋友圈之前的数据存放
     * @param req
     * @param resp
     */
    public void toLookMessage(HttpServletRequest req, HttpServletResponse resp){
        int type = Integer.parseInt(req.getParameter("type"));
        int id = Integer.parseInt(req.getParameter("id"));
        req.getSession().setAttribute("historyType",type);
        req.getSession().setAttribute("historyId",id);
    }

    /**
     * 看历史消息时的页面跳转
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    public void lookMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Object historyType = req.getSession().getAttribute("historyType");
        if (historyType != null){
            req.getRequestDispatcher("/WEB-INF/history.html").forward(req,resp);
        } else {
            resp.getWriter().write("你操作不当，出现问题");
        }
    }

    /**
     * 得到历史消息信息
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getHistoryMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer historyType = (Integer) req.getSession().getAttribute("historyType");
        Integer id = (Integer) req.getSession().getAttribute("historyId");
        if (historyType == null || id == null){
            resp.getWriter().write("你操作不当，出现问题");
        } else {
            PageDirector pageDirector = ToObjectUtil.getObject(req,PageDirector.class);
            User user = (User) req.getSession().getAttribute("user");
            Map select = pageDirector.getSelect();
            select.put("user",user);
            select.put("type",historyType);
            select.put("id",id);
            Page page = pageDirector.buildMessagePage();
            MyResult myResult = MyResult.build().setCode(200).setData(page).add("mtype",historyType).add("id",id);
            if (historyType == 1){
                User friend = userService.getUserById(id);
                myResult.add("friend",friend);
            }

            resp.getWriter().write(myResult.toJson());
        }
    }
}
