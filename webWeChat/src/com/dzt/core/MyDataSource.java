package com.dzt.core;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MyDataSource implements MydataSourceInterface {
    private int maxActiveSize = 50 ;
    private int initSize = 30 ;
    private int waitTime = 30000;
    private String url;
    private String username;
    private String password;
    private Object monitor = new Object();

    private final List<Connection> idleConnections = new ArrayList<>();
    private final List<Connection> activeConnections = new ArrayList<>();

    public MyDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    public MyDataSource(Properties properties) {
        getProperties(properties);
    }

    public void getProperties(Properties property){
        url = property.getProperty("url");
        username = property.getProperty("username");
        password = property.getProperty("password");
        if (property.contains("initSize")){
            initSize = Integer.parseInt(property.getProperty("initSize"));
        }
        if (property.contains("maxActiveSize")){
            maxActiveSize = Integer.parseInt(property.getProperty("maxActiveSize"));
        }
        if (property.contains("waitTime")) {
            waitTime = Integer.parseInt(property.getProperty("waitTime"));
        }
    }

    @Override
    public Connection getConnection(){
        Connection connection = null;
            while (connection == null){
                synchronized (monitor){
                    if (idleConnections.size() != 0){
                        connection = idleConnections.remove(0);
                    } else if ( idleConnections.size() >= initSize){

                    } else {
                        if (activeConnections.size() <= maxActiveSize){
                            connection = getProxyConnection();
                        }
                    }
                    if (connection == null){
                        try {
                            System.out.println("have no connection we are finding");
                            monitor.wait(waitTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }

            }

        if (connection == null){
            throw new RuntimeException("获得连接出现了问题");
        } else {
            activeConnections.add(connection);
        }
        return connection;
    }

    private void init(){

    }


    private Connection getRealConnection(){
        Connection connection = null;
        try {
            Driver driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return connection;
    }


    private Connection getProxyConnection(){
        ConnectionHandle connectionHandle = new ConnectionHandle(this,getRealConnection());
        return connectionHandle.getProxyConnection();
    }

    public void closeConnection(Connection proxy) {
        synchronized (monitor){
            for (int i = 0; i < activeConnections.size(); i++) {
                if (activeConnections.get(i) == proxy){
                    activeConnections.remove(i);
                    break;
                }
            }
            idleConnections.add(proxy);
            monitor.notifyAll();
        }
    }

    public List<Connection> getIdleConnections() {
        return idleConnections;
    }

    public List<Connection> getActiveConnections() {
        return activeConnections;
    }

    public Object getMonitor() {
        return monitor;
    }

    public int getInitSize() {
        return initSize;
    }

    public static void main(String[] args) {
        MyDataSource myDataSource =(MyDataSource) DataSourceFactory.creatDataSource(DataSourceFactory.WEB_DATASOURCE);
        Connection connection = myDataSource.getRealConnection();
    }

}
