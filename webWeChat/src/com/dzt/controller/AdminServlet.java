package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.bean.page.Page;
import com.dzt.bean.page.PageDirector;
import com.dzt.bean.selectBean.Publisher;
import com.dzt.controller.message.Msg;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.dao.UserDao;
import com.dzt.entity.*;
import com.dzt.service.*;
import com.dzt.service.impl.*;
import com.dzt.util.ImageCode;
import com.dzt.util.PatternUtils;
import com.dzt.util.StringUtil;
import com.dzt.util.ToObjectUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import javax.enterprise.inject.spi.Bean;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
@WebServlet("/admin/*")
public class AdminServlet extends MyServlet{
    private static String[] white = new String[]{"verify","login"};
    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);
    private AdminService adminService = BeanFactory.getBean(AdminServiceImpl.class);
    private MomentService momentService = BeanFactory.getBean(MomentServiceImpl.class);
    private MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);


    @Override
    public boolean filter(HttpServletRequest req) {
        String pathInfo = req.getPathInfo().substring(1);
        for (String s : white) {
            if (pathInfo.equals(s)){
                return true;
                sotu11111
            }
        }
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * ?????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    public void verify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletOutputStream outputStream = resp.getOutputStream();
        String s = ImageCode.outPut(outputStream);
        HttpSession session = req.getSession();
        session.setAttribute("adminVerify",s);
    }

    /**
     * ???????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void checkUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        PrintWriter writer = resp.getWriter();

        if ( !adminService.checkUser(username) ){
            writer.write(MyResult.build().setMsg("???????????????").setCode(200).add("isHave",false).toJson());
        } else {
            writer.write(MyResult.build().setCode(300).setMsg("?????????????????????").add("isHave",true).toJson());
        }
    }

    /**
     * ???????????????????????????
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/creatAdmin.html").forward(req,resp);
    }

    /**
     * ?????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        PrintWriter writer = resp.getWriter();
        Admin admin = (Admin) req.getSession().getAttribute("admin");
        if (admin.getPower() != 1){
            writer.write(MyResult.build().setCode(308).setMsg("?????????????????????????????????").toJson());
        } else if (adminService.checkUser(username)){
            writer.write(MyResult.build().setMsg("?????????????????????").setCode(308).toJson());
        } else if (PatternUtils.passwordCheck(password) && PatternUtils.usernameCheck(username)){
            writer.write(MyResult.build().setCode(200).setMsg("ok").toJson());
            adminService.add(new Admin(username,DigestUtils.md5Hex(password),0));
        } else {
            writer.write(MyResult.build().setCode(308).setMsg("???????????????????????????").toJson());
        }
    }


    /**
     * ??????????????????
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String verifyCode = req.getParameter("verifyCode");
        String adminVerify = (String) req.getSession().getAttribute("adminVerify");
        Admin admin = null;
        if (adminVerify.equals(verifyCode) && (admin = adminService.login(username,password)) != null){
            req.getSession().setAttribute("admin",admin);
            req.getRequestDispatcher("/WEB-INF/adminMenu.html").forward(req,resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/fail.jsp").forward(req,resp);
        }
    }

    /**
     * ?????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void toLookUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = ToObjectUtil.getObject(req,User.class);
        User lookUser = userService.getUserById(user.getId());
        req.getSession().setAttribute("lookUser",lookUser);
        resp.getWriter().write(MyResult.build().setCode(200).toJson());
    }

    /**
     * ????????????????????????????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    public void lookUserDo(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("lookUser");
        if (user != null){
            req.getRequestDispatcher("/WEB-INF/admin/lookInformation.html").forward(req,resp);
        } else {
            resp.getWriter().write("505");
        }
    }

    /**
     * ?????????????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getUserInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("lookUser");
        resp.getWriter().write(MyResult.build().setCode(200).setData(user).toJson());
    }

    /**
     * ????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void removeSeal(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("lookUser");
        userService.removeSeal(user);
        resp.getWriter().write(MyResult.build().setMsg("????????????").setCode(200).toJson());
    }

    /**
     * ??????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void sealUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String date = req.getParameter("date");
        if (PatternUtils.numberCheck(date) && StringUtil.notEmpty(date)){
            User user = (User) req.getSession().getAttribute("lookUser");
            userService.seal(user,Integer.parseInt(date));
            if (Msg.getWebSocketMap().containsKey(user.getId())) {
                Msg msg = Msg.getWebSocketMap().get(user.getId());
                msg.sendMessage(new Message(15));
            }
            resp.getWriter().write(MyResult.build().setMsg("????????????").setCode(200).toJson());
        } else {
            MyResult myResult = MyResult.build().setMsg("??????????????????").setCode(309);
            resp.getWriter().write(myResult.toJson());
        }
    }


    /**
     * ????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getUserMoment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("lookUser");
        PageDirector pageDirector = ToObjectUtil.getObject(req,PageDirector.class);
        pageDirector.getSelect().put("adminToLook",user);
        Page<Publisher> publisherPage = pageDirector.BuildMomentPage();
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("?????????").setData(publisherPage).toJson());
    }

    /**
     * ?????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void deleteMoment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = ToObjectUtil.getObject(req,Publisher.class);
        User user = (User) req.getSession().getAttribute("user");
        momentService.remove(publisher.getId());
        resp.getWriter().write(MyResult.build().setMsg("????????????").toJson());
    }

    /**
     * ?????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void toLookMoment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = ToObjectUtil.getObject(req,Publisher.class);
        req.getSession().setAttribute("adminMoment",publisher);
        resp.getWriter().write(MyResult.build().setCode(200).toJson());
    }

    /**
     * ????????????????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    public void toLookMomentDo(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("/WEB-INF/admin/lookMoment.html").forward(req,resp);

    }

    /**
     * ????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = (Publisher) req.getSession().getAttribute("adminMoment");
        Map map = new HashMap<>();
        map.put("publisher",publisher);
        MomentLikeService momentLikeService = BeanFactory.getBean(MomentServiceImpl.class);
        Integer integer = momentLikeService.getLikeCount(publisher.getId());
        map.put("like",integer);
        resp.getWriter().write(MyResult.build().setData(map).toJson());
    }


    /**
     * ????????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Publisher publisher = (Publisher) req.getSession().getAttribute("adminMoment");
        CommentService commentService = BeanFactory.getBean(CommentServiceImpl.class);
        String res = commentService.getMomentComment(publisher.getId());
        resp.getWriter().write(res);
    }

    /**
     * ?????????????????????
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getReport(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Message> messages = messageService.getReport();
        resp.getWriter().write(MyResult.build().setCode(200).setData(messages).toJson());
    }

    /**
     * ???????????????????????????
     * @param req
     * @param resp
     */
    @ToJSON
    public void deleteReport(HttpServletRequest req, HttpServletResponse resp){
        int id = Integer.parseInt(req.getParameter("id"));
        messageService.removeById(id);
    }

}
