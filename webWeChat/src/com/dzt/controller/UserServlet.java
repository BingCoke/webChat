package com.dzt.controller;

import com.dzt.bean.MyMail;
import com.dzt.bean.MyResult;
import com.dzt.bean.page.Page;
import com.dzt.bean.page.PageDirector;

import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.service.MessageService;
import com.dzt.service.UserService;
import com.dzt.service.impl.MessageServiceImpl;
import com.dzt.service.impl.UserServiceImpl;
import com.dzt.util.ImageCode;
import com.dzt.util.PatternUtils;
import com.dzt.util.StringUtil;
import com.dzt.util.ToObjectUtil;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.resources.Messages;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@MultipartConfig
@WebServlet("/user/*")
public class UserServlet  extends MyServlet{

    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);
    private MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);

    private static String[] white = new String[]{"verify","userToLogin","autoLogin","checkUser","userRegister","sendRegisterCode","findProfile","findPassword","sendMailToFindPassword","getUser"};
    @Override
    public boolean filter(HttpServletRequest req) {
        String pathInfo = req.getPathInfo().substring(1);
        User user = (User) req.getSession().getAttribute("user");
        for (String s : white) {
            if (pathInfo.equals(s)){
                return true;
            }
        }
        if (user == null || user.getPower() == 0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 寻找用户名是否存在
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void checkUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        PrintWriter writer = resp.getWriter();
        Boolean is = userService.hasUsername(username);
        if(is == true){
            writer.write(MyResult.build().setCode(200).setMsg("已经有了相同的用户名").add("username",username).add("isHave",true).toJson());
        } else {
            writer.write(MyResult.build().setCode(200).setMsg("用户名合法").add("username",username).add("isHave",false).toJson());
        }
    }

    /**
     * 用户注册
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    @ToJSON
    public void userRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        UserService userService = BeanFactory.getBean(UserServiceImpl.class);
        String password = req.getParameter("password");
        String passwordAgain = req.getParameter("passwordAgain");
        User user = ToObjectUtil.getObject(req,User.class);
        String inputMailCode = req.getParameter("mailCode");
        String mailCode = (String) session.getAttribute("mailCode");
        user.setProfile("");
        Part profile = req.getPart("profile");
        //后台验证邮箱验证码是否正确以及二次判断输入的数据是否正确
        if (!inputMailCode.equals(mailCode)){
            resp.getWriter().write(MyResult.build().setCode(305).setMsg("邮箱验证码错误或者失效").toJson());
        } else if(PatternUtils.usernameCheck(user.getUsername()) &&
                PatternUtils.passwordCheck(user.getPassword()) &&
                PatternUtils.mailCheck(user.getMail()) &&
                password.equals(passwordAgain) &&
                !userService.hasUsername(user.getUsername()) &&
                PatternUtils.phoneCheck(user.getPhone())){
            userService.register(user);
            userService.saveProfile(profile,user.getUsername());
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("注册成功!").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(300).setMsg("请检查输入的信息").toJson());
        }
    }


    /**
     * 注册时发送验证码
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void sendRegisterCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String mail = req.getParameter("mail");
        String code = MyMail.getCode();
        req.getSession().setAttribute("mailCode",code);
        HttpSession session = req.getSession();
        Boolean is = userService.checkMail(mail);
        if (is){
            resp.getWriter().write(MyResult.build().setCode(306).setMsg("该邮箱已经被注册过了呢").toJson());
        }else {
            ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
            MyMail.sendRegisterMsg(mail,code);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    session.setAttribute("mailCode", UUID.randomUUID().toString());
                }
            };
            timer.schedule(runnable,60*1000, TimeUnit.MILLISECONDS);
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("成功！").toJson());
        }
    }


    /**
     * 登录时的验证码
     * @param req
     * @param resp
     * @throws IOException
     */
    public void verify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletOutputStream outputStream = resp.getOutputStream();
        String s = ImageCode.outPut(outputStream);
        HttpSession session = req.getSession();
        session.setAttribute("verify",s);
    }

    /**
     * 如果输入的验证码找到了用户显示用户的头像
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void findProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        User user = userService.selectByUsername(username);
        if ( user != null){
            resp.getWriter().write(MyResult.build().setCode(200).setMsg(user.getProfile()).add("isHave",true).toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(307).add("isHave",false).toJson());
        }
    }


    /**
     * 用户登录
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void userToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String inputVerify = req.getParameter("verifyCode");
        HttpSession session = req.getSession();
        String verify =(String)session.getAttribute("verify");
        if ( !inputVerify.equals(verify)){
            resp.getWriter().write(MyResult.build().setMsg("验证码错误").setCode(305).toJson());
        } else {
            List result = userService.login(username,password);
            resp.getWriter().write((String)result.get(0));
            if ( result.size() > 1){
                User user = (User) result.get(1);
                if (user.getPower() != 0){
                    Map map = userService.getGroupAuthoritiesMap(user.getId());
                    Map map1 = userService.getFriendAuthoritiesMap(user.getId());
                    req.getSession().setAttribute("userAuthorities",map1);
                    req.getSession().setAttribute("groupAuthorities",map);
                    req.getSession().setAttribute("user",user);
                }
            }
        }
    }

    /**
     * 自动登录
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void autoLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = BeanFactory.getBean(UserServiceImpl.class);
        User user = ToObjectUtil.getObject(req,User.class);
        List result = userService.login(user.getUsername(),user.getPassword());
        if ( result.size() > 1){
            User user1 = (User) result.get(1);
            if (user1.getPower() != 0){
                Map map = userService.getGroupAuthoritiesMap(user1.getId());
                Map map1 = userService.getFriendAuthoritiesMap(user.getId());
                req.getSession().setAttribute("userAuthorities",map1);
                req.getSession().setAttribute("groupAuthorities",map);
                req.getSession().setAttribute("user",user1);
            } else {
                User user2 = new User(0);
                req.getSession().setAttribute("user",user2);
            }
        }
        resp.getWriter().write((String) result.get(0));
    }



    /**
     * 找回密码的时候发送邮箱验证码
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void sendMailToFindPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        User user = userService.selectByUsername(username);
        if (user == null){
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("找不到用户").toJson());
        } else {
            String mail = user.getMail();
            String code = MyMail.getCode();
            MyMail.sendFindMsg(mail,code);
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("已经向你的邮箱发送验证码").toJson());
            HttpSession session = req.getSession();
            session.setAttribute("findPasswordCode",code);
            session.setAttribute("userIdToFindPassword",user.getId());
        }
    }


    /**
     * 找回密码接口
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void findPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        String findPasswordCode = (String) session.getAttribute("findPasswordCode");
        if (StringUtil.notEmpty(findPasswordCode)){
            String password = req.getParameter("password");
            String passwordAgain = req.getParameter("passwordAgain");
            String code = req.getParameter("code");
            if (code.equals(findPasswordCode)){
                if ( !password.equals(passwordAgain)){
                    writer.write(MyResult.build().setCode(20003).setMsg("输入密码不一致").toJson());
                } else if (!PatternUtils.passwordCheck(password)){
                    writer.write(MyResult.build().setCode(309).setMsg("密码格式不正确").toJson());
                } else {
                    int userId = (int) session.getAttribute("userIdToFindPassword");
                    userService.updatePassword(password,userId);
                    writer.write(MyResult.build().setCode(200).setMsg("成功修改密码").toJson());

                }
            } else {
                writer.write(MyResult.build().setCode(305).setMsg("验证码错误").toJson());
            }
        } else {
            writer.write(MyResult.build().setCode(307).setMsg("请先发送验证码").toJson());
        }
    }



    /**
     * 查询用户的接口
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void findUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageDirector pageDirector = ToObjectUtil.getObject(req,PageDirector.class);
        String username = req.getParameter("username");
        String name = req.getParameter("name");
        PrintWriter writer = resp.getWriter();
        if (StringUtil.notEmpty(name)){
            pageDirector.getSelect().put("name",name);
        } else if(StringUtil.notEmpty(username)){
            pageDirector.getSelect().put("username",username);
        }
        if (StringUtil.notEmpty(username) || StringUtil.notEmpty(name)){
            Page<User> userPage = pageDirector.BuildUserPage();
            if (userPage.getTotalDateSize() == 0){
                writer.write(MyResult.build().setCode(2002).setMsg("得到数据为空").toJson());
            } else {
                writer.write(MyResult.build().setCode(200).setMsg("成功！").add("data",userPage).toJson());
            }
        }  else {
            writer.write(MyResult.build().setCode(2001).setMsg("输入数据为空").toJson());
        }
    }

    /**
     * 查找一个用户的接口
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void selectUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        User user = userService.getUserById(id);
        user.setPower(0);
        user.setPassword("");
        user.setMail("");
        user.setPhone("");
        user.setBack("");
        resp.getWriter().write(MyResult.build().setCode(200).setData(user).toJson());
    }
    /**
     * 得到会话中的user的接口
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        User user = (User) session.getAttribute("user");
        if (user == null){
            writer.write(MyResult.build().setCode(501).toJson());
        } else {
            if (user.getPower() == 0){
                writer.write(MyResult.build().setCode(200).setData(user).toJson());
            } else {
                String result = userService.selectByUserId(user.getId());
                writer.write(result);
            }

        }
    }

    /**
     * 更新用户个人信息的接口
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        User user = (User) req.getSession().getAttribute("user");
        user.setName(name);
        user.setGender(gender);
        user.setPhone(phone);
        String r = userService.updateUserInformation(name,gender,phone,user.getId());
        resp.getWriter().write(r);
    }

    /**
     * 更新用户头像的接口
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    @ToJSON
    public void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        Collection<Part> parts = req.getParts();
        User user = (User) req.getSession().getAttribute("user");
        Part file = req.getPart("file");
        String s = userService.saveProfile(file,user.getUsername());
        user.setProfile(s);
        resp.getWriter().write(MyResult.build().setMsg("ok").setCode(200).setData(s).toJson());
        user.setProfile(s);
    }

    /**
     * 更新用户信息的页面的转发
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/user/updateUser.html").forward(req,resp);
    }


    /**
     * 更改密码
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String password = req.getParameter("password");
        String passwordAgain = req.getParameter("passwordAgain");
        String oldPassword = req.getParameter("oldPassword");
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user.getPassword().equals(DigestUtils.md5Hex(oldPassword))){
            if ( !password.equals(passwordAgain)){
                writer.write(MyResult.build().setCode(20003).setMsg("输入密码不一致").toJson());
            } else if (!PatternUtils.passwordCheck(password)){
                writer.write(MyResult.build().setCode(309).setMsg("密码格式不正确").toJson());
            } else {
                userService.updatePassword(password,user.getId());
                writer.write(MyResult.build().setCode(200).setMsg("成功修改密码").toJson());
            }
        } else {
            writer.write(MyResult.build().setCode(308).setMsg("并不是旧密码").toJson());
        }
    }


    /**
     * 更改邮箱
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void updateMail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String newMailCode = (String) req.getSession().getAttribute("newMailCode");
        String newMail = (String) req.getSession().getAttribute("newMail");
        String newCode = req.getParameter("newCode");
        if (newMailCode.equals(newCode)){
            User user = (User) req.getSession().getAttribute("user");
            user.setMail(newMail);
            userService.updateMail(newMail,user.getId());
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("成功修改！").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(305).setMsg("验证码错误").toJson());
        }

    }

    /**
     * 更改邮箱时发送邮件到新邮箱
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void sendToNewMail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String newMail = req.getParameter("newMail");
        String code = req.getParameter("code");
        String oldMailCode = (String) req.getSession().getAttribute("oldMailCode");
        if (StringUtil.notEmpty(newMail) || StringUtil.notEmpty(code)){
            if (oldMailCode.equals(code)){
                if (PatternUtils.mailCheck(newMail)){
                    String newMailCode = userService.sendToNewMailCode(newMail);
                    req.getSession().setAttribute("newMailCode",newMailCode);
                    req.getSession().setAttribute("newMail",newMail);
                    resp.getWriter().write(MyResult.build().setCode(200).setMsg("正在给新邮箱发送验证码").toJson());
                } else {
                    resp.getWriter().write(MyResult.build().setCode(308).setMsg("请输入正确的邮箱地址").toJson());
                }
            } else {
                resp.getWriter().write(MyResult.build().setCode(305).setMsg("验证码错误").toJson());
            }
        } else {
            resp.getWriter().write(MyResult.build().setCode(20001).setMsg("请不要输入空数据").toJson());
        }
    }

    /**
     * 发送邮件到旧邮箱
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void toUpdateMail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        String mailCode = userService.sendMailToUpdate(user.getId());
        req.getSession().setAttribute("oldMailCode",mailCode);
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("发送了验证码至你的旧邮箱").toJson());
    }

    /**
     * 得到用户间好友的权限
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getAuthorities(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        Map map = userService.getFriendAuthoritiesMap(user.getId());
        req.getSession().setAttribute("userAuthorities",map);
        resp.getWriter().write(MyResult.build().setCode(200).setData(map).toJson());
    }

    /**
     * 好友间的拉黑
     * @param req
     * @param resp
     * @throws Exception
     */
    @ToJSON
    public void updateBack(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = (User) req.getSession().getAttribute("user");
        Part file = req.getPart("file");
        String url = userService.savaBack(file,user);
        user.setBack(url);
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("ok").add("url",url).toJson());
    }

    /**
     * 用户头像的更新
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    @ToJSON
    public void saveFile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part file = req.getPart("file");

        String path = userService.saveFile(file);
        Map map = new HashMap();
        map.put("src","/file/" + path);
        map.put("fileName",file.getSubmittedFileName());
        resp.getWriter().write(MyResult.build().setCode(0).setMsg("").setData(map).toJson());
    }


    /**
     * 用户举报页面跳转
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void toReport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int i = Integer.parseInt(req.getParameter("id"));
        req.getSession().setAttribute("reportId",i);
        req.getRequestDispatcher("/WEB-INF/user/report.html").forward(req,resp);
    }

    /**
     * 用户举报
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void report(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = (int) req.getSession().getAttribute("reportId");
        String content = req.getParameter("content");
        User user = (User) req.getSession().getAttribute("user");
        messageService.addMessage(new Message(user.getId(),id,6,content));
        resp.getWriter().write(MyResult.build().setCode(200).toJson());
    }


}
