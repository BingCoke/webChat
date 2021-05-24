package com.dzt.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Z
 */
public interface AOPMethod {
    /**
     * 方法调用前
     */
    void before(Object proxy, Method method, Object[] args) ;

    /**
     * 方法调用后
     */
    void after(Object proxy, Method method, Object[] args) ;

    /**
     * 方法最后调用
     * @param proxy
     * @param method
     * @param args
     */
    void last(Object proxy, Method method, Object[] args);

    /**
     * 方法抛出异常后
     *
     * @param proxy
     * @param method
     * @param args
     */
    default void throwing(Object proxy, Method method, Object[] args)  {
        System.out.println("出现异常啦");
    }

}
