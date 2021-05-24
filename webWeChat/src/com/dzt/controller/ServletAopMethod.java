package com.dzt.controller;

import com.dzt.bean.MyResult;
import com.dzt.core.AOPMethod;
import com.dzt.core.ToJSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class ServletAopMethod implements AOPMethod {

    private HttpServletResponse resp;
    private HttpServletRequest req;

    public ServletAopMethod(HttpServletResponse resp, HttpServletRequest req) {
        this.resp = resp;
        this.req = req;
    }

    @Override
    public void before(Object proxy, Method method, Object[] args) {
        if (method.isAnnotationPresent(ToJSON.class)){
            resp.addHeader("content-type","application/json;charset=utf-8");
        }
    }
    @Override
    public void after(Object proxy, Method method, Object[] args)  {
    }
    @Override
    public void last(Object proxy, Method method, Object[] args) {
        if (method.isAnnotationPresent(ToJSON.class)){
            try {
                resp.getWriter().flush();
                resp.getWriter().close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
    @Override
    public void throwing(Object proxy, Method method, Object[] args) {
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        resp.addHeader("content-type","application/json;charset=utf-8");
        writer.write(MyResult.build().setCode(500).setMsg("后台发生了错误").toJson());
        writer.flush();
    }
}
