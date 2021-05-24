package com.dzt.core;

import com.dzt.controller.ServletAopMethod;
import net.sf.cglib.proxy.Enhancer;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Session {
    private Connection connection;
    private Map<String,String> entityMap;
    private Map<String, Map<String  ,DaoWrapper>> env;
    private DataSource dataSource;

    public Session(DataSource dataSource, Map<String, Map<String, DaoWrapper>> env, Map<String,String> entityMap) throws SQLException {
        this.dataSource = dataSource;
        this.env = env;
        this.entityMap = entityMap;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void begin(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void commit(){
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void rollback(){
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public <T> T getMapper(Class clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new SqlInvocationHandler(dataSource,env.get(clazz.getName()),entityMap.get(clazz.getName())));
        Object o = enhancer.create();
        T t = (T) o;
        return t;
    }

    public <T> T getMapperSession(Class clazz) throws SQLException {
        T t = (T) Proxy.newProxyInstance(Session.class.getClassLoader(), new Class[]{clazz},new SqlInvocationHandler(dataSource.getConnection(),env.get(clazz.getName()),entityMap.get(clazz.getName())));
        return t;
    }



    @Override
    public String toString() {
        return "Session{" +
                "connection=" + connection +
                '}';
    }
}
