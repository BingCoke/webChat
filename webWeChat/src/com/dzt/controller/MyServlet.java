package com.dzt.controller;

import com.dzt.core.AOPHandle;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import net.sf.cglib.proxy.Enhancer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.*;

/**
 * @author Z
 */

public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doMy(req,resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doMy(req,resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public boolean filter(HttpServletRequest req){
        return true;
    }

    private void doMy(HttpServletRequest req, HttpServletResponse resp) throws IllegalAccessException, InstantiationException {

        Class clazz = this.getClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new AOPHandle(this,new ServletAopMethod(resp,req)));
        Object o = enhancer.create();
        String str = req.getPathInfo();
        Method method = null;
        Method filter = null;

        try {
            filter = clazz.getMethod("filter",new Class[]{HttpServletRequest.class});
            method = clazz.getMethod(str.substring(1), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //看一下对应的方法是否存在
        if (method != null){
            try {
                if ((boolean)filter.invoke(o,req)){
                    method.invoke (o,req,resp);
                } else {
                    resp.sendRedirect("/index.jsp");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            resp.setStatus(404);
        }
    }
}
