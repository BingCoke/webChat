package com.dzt.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 用于得到代理连接和拦截关闭连接函数
 */
public class ConnectionHandle implements InvocationHandler {
    private MyDataSource myDataSource;
    private Connection realConnection;
    private Connection proxyConnection;

    public ConnectionHandle(MyDataSource myDataSource, Connection realConnection) {
        this.myDataSource = myDataSource;
        this.realConnection = realConnection;
        proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),new Class[]{Connection.class},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("close")){
                if (myDataSource.getIdleConnections().size() >= myDataSource.getInitSize()){
                    realConnection.close();
                } else {
                    myDataSource.closeConnection(proxyConnection);
                }
        } else {
          return method.invoke(realConnection,args);
        }
        return null;
    }


    public Connection getProxyConnection() {
        return proxyConnection;
    }
}
