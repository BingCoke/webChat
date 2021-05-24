package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.Commenter;
import com.dzt.bean.selectBean.Publisher;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.Comment;
import com.dzt.entity.User;
import com.dzt.service.CommentService;
import com.dzt.service.impl.CommentServiceImpl;
import com.dzt.util.ToObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Z
 */
@WebServlet("/comment/*")
public class CommentServlet extends MyServlet {
    private CommentService commentService = BeanFactory.getBean(CommentServiceImpl.class);

    @Override
    public boolean filter(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getPower() == 0 && user.getPower() == 2){
            return false;
        } else {
            return true;
        }
    }

    @ToJSON
    public void sendComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = (Publisher) req.getSession().getAttribute("publishToLook");
        User user = (User) req.getSession().getAttribute("user");
        String type = req.getParameter("ctype");
        String content = req.getParameter("content");
        if (Integer.parseInt(type) == 1){
            commentService.add(new Comment(content,publisher.getId(),user.getId()));
        } else {
            commentService.add(new Comment(content,publisher.getId(),user.getId(),Integer.parseInt(req.getParameter("mid"))));
        }
        resp.getWriter().write(MyResult.build().setCode(200).toJson());
    }


    @ToJSON
    public void getComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = (Publisher) req.getSession().getAttribute("publishToLook");
        String res = commentService.getMomentComment(publisher.getId());
        resp.getWriter().write(res);
    }

    @ToJSON
    public void deleteComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Commenter commenter = ToObjectUtil.getObject(req,Commenter.class);
        commentService.remove(commenter.getId());
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("ok").toJson());
    }




}
