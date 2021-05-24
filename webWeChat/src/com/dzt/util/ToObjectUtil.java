package com.dzt.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 封装对象并获取
 * @author Z
 */
public class ToObjectUtil {

    /**
     * 通过获得的request请求，得到类对象，然后封装成对象 并返回,
     * @param req
     * @param clazz
     * @return
     */
    public static <T> T getObject(HttpServletRequest req, Class clazz){
        Object o = null;
        try {
             o = clazz.newInstance();
            Field[] declaredFields = clazz.getSuperclass().getDeclaredFields();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                o = set(field,o,req);
            }

            for (Field field : declaredFields) {
                field.setAccessible(true);
                o = set(field,o,req);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T)o;
    }

    private static Object set(Field field,Object o,HttpServletRequest req) throws IllegalAccessException {
        if ( req.getParameter(field.getName()) == null || req.getParameter(field.getName()).trim().isEmpty() ){
            return o;
        }
        if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
            field.set(o,Integer.parseInt(req.getParameter(field.getName()).trim()));
        } else if (Double.class.equals(field.getType()) || Double.class.equals(field.getType()) ) {
            field.set(o,Double.parseDouble(req.getParameter(field.getName()).trim()));
        } else if (Float.class.equals(field.getType()) || float.class.equals(field.getType()) ){
            field.set(o,Float.parseFloat(req.getParameter(field.getName()).trim()));
        } else if(String.class.equals(field.getType())){
            field.set(o,req.getParameter(field.getName()));
        }
        return o;
    }

}
