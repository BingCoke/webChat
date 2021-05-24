package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.bean.page.Page;
import com.dzt.bean.page.PageDirector;
import com.dzt.bean.selectBean.Publisher;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.Moment;
import com.dzt.entity.User;
import com.dzt.service.CommentService;
import com.dzt.service.MomentLikeService;
import com.dzt.service.MomentService;
import com.dzt.service.UnLookService;
import com.dzt.service.impl.CommentServiceImpl;
import com.dzt.service.impl.MomentLikeServiceImpl;
import com.dzt.service.impl.MomentServiceImpl;
import com.dzt.service.impl.UnLookServiceImpl;
import com.dzt.util.ToObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;

/**
 * @author Z
 */
@WebServlet("/moment/*")
public class MomentServlet extends MyServlet {
    private MomentService momentService = BeanFactory.getBean(MomentServiceImpl.class);
    private UnLookService unLookService = BeanFactory.getBean(UnLookServiceImpl.class);
    private MomentLikeService momentLikeService = BeanFactory.getBean(MomentLikeServiceImpl.class);
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
    /**
     * 发布一个朋友圈
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void publishMoment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Map<String, String[]> parameterMap = req.getParameterMap();
        Set<String> strings = parameterMap.keySet();
        String[] content = parameterMap.get("myMoment");
        String[] friends = parameterMap.get("friend");
        momentService.addMoment(content[0],friends,user);
        resp.getWriter().write(MyResult.build().setMsg("成功发布").toJson());
    }

    /**
     * 得到好友的朋友圈
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getFriendsMoment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageDirector pageDirector = ToObjectUtil.getObject(req,PageDirector.class);
        User user = (User) req.getSession().getAttribute("user");
        pageDirector.getSelect().put("user",user);
        Page<Publisher> publisherPage = pageDirector.BuildMomentPage();
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("成功！").add("data",publisherPage).toJson());
    }

    /**
     * 删除一个朋友圈
     * @param req
     * @param resp
     */
    @ToJSON
    public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = ToObjectUtil.getObject(req,Publisher.class);
        User user = (User) req.getSession().getAttribute("user");
        if (publisher.getUserId().equals(user.getId())){
            commentService.removeByMomentId(publisher.getId());
            unLookService.removeByMomentId(publisher.getId());
            momentLikeService.removeByMomentId(publisher.getId());
            momentService.remove(publisher.getId());
            resp.getWriter().write(MyResult.build().setMsg("删除成功").toJson());
        }else {
            resp.getWriter().write(MyResult.build().setMsg("没有权限删除").toJson());
        }

    }


    /**
     * 朋友圈查看前检查是否存在该朋友圈并存放
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void toLookMoment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = ToObjectUtil.getObject(req,Publisher.class);
        Moment moment = momentService.get(publisher.getId());
        if (moment.getId().equals(publisher.getId())){
            req.getSession().setAttribute("publishToLook",publisher);
            resp.getWriter().write(MyResult.build().setCode(200).toJson());
        } else {
           resp.getWriter().write(MyResult.build().setData(404).setMsg("没有找到该条朋友圈").toJson());
        }
    }

    /**
     * 看朋友圈信息时页面跳转
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toLookDo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/lookMoment.html").forward(req,resp);
    }

    /**
     * 得到该朋友圈的信息
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Publisher publisher = (Publisher) req.getSession().getAttribute("publishToLook");
        Integer integer = momentLikeService.getLikeCount(publisher.getId());
        boolean isLike = momentLikeService.isLike(publisher.getId(),user.getId());
        Map map = new HashMap(8);
        map.put("publisher",publisher);
        map.put("like",integer);
        map.put("isLike",isLike);
        resp.getWriter().write(MyResult.build().setCode(200).setData(map).toJson());
    }

    /**
     * 点赞时接口
     * @param req
     * @param resp
     */
    @ToJSON
    public void like(HttpServletRequest req, HttpServletResponse resp){
        Integer like = Integer.parseInt(req.getParameter("like"));
        Publisher publisher = (Publisher) req.getSession().getAttribute("publishToLook");
        User user = (User) req.getSession().getAttribute("user");
        if (like == 1){
            momentLikeService.addLike(publisher.getId(),user.getId());
        } else {
            momentLikeService.removeLike(publisher.getId(),user.getId());
        }
    }

    


}
