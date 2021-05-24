package com.dzt.core;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

public class BeanFactory {
    /**
     * 获得代理对象
     * @param <T>
     * @param clazz
     * @return
     */
    private static  <T> T getBeanObject(Class clazz, AOPMethod aopMethod) throws IllegalAccessException, InstantiationException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new AOPHandle(clazz.newInstance(),aopMethod));
        return (T) enhancer.create();
    }



    public static  <T> T getBean(Class clazz, AOPMethod aopMethod){
        T t = null;
        try {
            t = getBeanObject(clazz,aopMethod);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  t;
    }

    public static  <T> T getBean(Class clazz){
        T t = null;
        try {
            t = getBeanObject(clazz,null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  t;
    }

}
