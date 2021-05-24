package com.dzt.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Proxy;

public class ServiceFactory {
    private Object o;



    /**
     * 获得代理对象
     * @param o
     * @param <T>
     * @return
     */
    private static  <T> T getBeanObject(Object o, AOPMethod aopMethod){

        Class clazz = o.getClass();
        return (T) Proxy.newProxyInstance(BeanFactory.class.getClassLoader(),o.getClass().getInterfaces(),new AOPHandle(o,aopMethod));
    }
}
