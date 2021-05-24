package com.dzt.core;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AOPHandle implements InvocationHandler, MethodInterceptor {
    private Object object;
    private AOPMethod aopMethod;

    public AOPHandle(Object o, AOPMethod aopMethod){
        this.object = o;
        this.aopMethod = aopMethod;
    }


    public AOPHandle(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //方法返回值
        Object ret=null;
        //反射调用方法
        if (aopMethod != null){
            try {
                aopMethod.before(proxy,method,args);
                ret=method.invoke(object, args);
                aopMethod.after(proxy,method,args);
            }catch (Exception e){
                System.out.println(method.getDeclaringClass().getName() +"_"+ method.getName() +"出现异常");
                aopMethod.throwing(proxy,method,args);
                e.printStackTrace();
            } finally {
                aopMethod.last(proxy, method, args);
            }
        } else {
            try {
                int i = 0;
                ret=method.invoke(object, args);
            } catch (Exception e){
                System.out.println(method.getDeclaringClass().getName() +"_"+ method.getName() +"出现异常");

                e.printStackTrace();
            }
        }
        //返回反射调用方法的返回值
        return ret;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //方法返回值
        Object ret=null;
        //反射调用方法
        if (aopMethod != null){
            try {
                aopMethod.before(object,method,objects);
                ret=method.invoke(object, objects);
                aopMethod.after(object,method,objects);
            }catch (Exception e){
                System.out.println(method.getDeclaringClass().getName() +"_"+ method.getName() +"出现异常");
                aopMethod.throwing(object,method,objects);
                e.printStackTrace();
            } finally {
                aopMethod.last(object, method, objects);
            }
        } else {
            try {
                ret=method.invoke(object, objects);
            } catch (Exception e){
                System.out.println(method.getDeclaringClass().getName() +"_"+ method.getName() +"出现异常");
                e.printStackTrace();
            }
        }
        //返回反射调用方法的返回值
        return ret;
    }
}
