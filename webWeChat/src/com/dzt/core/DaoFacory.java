package com.dzt.core;


import com.dzt.controller.ServletAopMethod;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author Z
 */
public class DaoFacory {
    private static SessionFactory webChatSessionFactory = new SessionFactory("config.xml");

    /**
     * 传入要得到dao的class类返回对应dao的代理对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T getWebChatDao(Class clazz){

        return webChatSessionFactory.openSession().getMapper(clazz);
    }

    /**
     * 传入要的得到的dao的类名返回对应的dao的代理对象
     * @param clazzName
     * @param <T>
     * @return
     */
    public static  <T> T getWebChatDao(String clazzName){
        Class clazz = null;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return webChatSessionFactory.openSession().getMapper(clazz);
    }




}
