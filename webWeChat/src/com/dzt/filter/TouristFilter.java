package com.dzt.filter;

import com.dzt.core.BeanFactory;
import com.dzt.entity.User;
import com.dzt.service.UserService;
import com.dzt.service.impl.UserServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Z
 */
@WebFilter("/tourist.html")
public class TouristFilter extends HttpFilter {
    private UserService userService = BeanFactory.getBean(UserServiceImpl.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = userService.add(new User((UUID.randomUUID() + "").substring(0,15),("游客" + UUID.randomUUID()).substring(0,11),2));
        ((HttpServletRequest)request).getSession().setAttribute("user",user);
        chain.doFilter(request,response);
    }
}
