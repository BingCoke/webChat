package com.dzt.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Z
 */
@WebFilter("/*")
public class CharsetFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletRequest)servletRequest).setCharacterEncoding("utf-8");
        ((HttpServletResponse)servletResponse).setContentType("utf-8");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
