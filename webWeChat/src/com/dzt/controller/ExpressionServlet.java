package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.core.BeanFactory;
import com.dzt.core.ToJSON;
import com.dzt.entity.Expression;
import com.dzt.entity.User;
import com.dzt.service.ExpressionService;
import com.dzt.service.UserService;
import com.dzt.service.impl.ExpressionServiceImpl;
import com.dzt.service.impl.UserServiceImpl;
import com.dzt.util.ToObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

/**
 * @author Z
 */
@MultipartConfig
@WebServlet("/expression/*")
public class ExpressionServlet extends MyServlet {
    private ExpressionService expressionService = BeanFactory.getBean(ExpressionServiceImpl.class);
    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);

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
     * 找出用户的表情
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void getMyExpression(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<Expression> expressions = expressionService.getUserExpression(user.getId());
        resp.getWriter().write(MyResult.build().setCode(200).setData(expressions).toJson());
    }

    /**
     * 增加一个用户的表情
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    @ToJSON
    public void saveExpression(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");
        Part file = req.getPart("file");
        String path = userService.saveFile(file);
        expressionService.add(new Expression(user.getId(),"/file/" + path));
        resp.getWriter().write(MyResult.build().setCode(200).setMsg("添加表情成功").toJson());
    }

    /**
     * 将表情删除
     * @param req
     * @param resp
     * @throws IOException
     */
    @ToJSON
    public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Expression expression = ToObjectUtil.getObject(req,Expression.class);
        Expression expression1 = expressionService.getById(expression.getId());
        User user = (User) req.getSession().getAttribute("user");
        if (expression1.getUserId().equals(user.getId())){
            expressionService.remove(expression1.getId());
            resp.getWriter().write(MyResult.build().setCode(200).setMsg("已经将表情删除").toJson());
        } else {
            resp.getWriter().write(MyResult.build().setCode(308).setMsg("你没有权限删除该表情").toJson());
        }
    }
}
