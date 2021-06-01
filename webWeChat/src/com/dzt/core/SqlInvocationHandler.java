package com.dzt.core;


import com.dzt.util.JdbcUtil;
import com.dzt.util.StringUtil;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlInvocationHandler implements InvocationHandler, MethodInterceptor {
    private DataSource dataSource;
    private Connection connection;
    private Map<String,DaoWrapper> wapper = new HashMap<>();
    private String entityClass;
    public static final String TYPE_INSERT = "insert";
    public static final String TYPE_UPDATE = "update";
    public static final String TYPE_SELECT = "select";
    public static final String TYPE_DELETE = "delete";

    public SqlInvocationHandler(DataSource dataSource, Map<String, DaoWrapper> wapper,String entityClass) {
        this.dataSource = dataSource;
        this.wapper = wapper;
        this.entityClass = entityClass;
    }

    public SqlInvocationHandler(Connection connection, Map<String, DaoWrapper> wapper, String entityClass) {
        this.connection = connection;
        this.wapper = wapper;
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            if (dataSource != null){
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
            }

            if (method.getDeclaringClass() == BaseDao.class){
                BaseDaoImpl baseDao = new BaseDaoImpl(connection,Class.forName(entityClass));
                Object o =  method.invoke(baseDao,args);
                connection.commit();
                return o;
            }

            DaoWrapper daoWrapper = wapper.get(method.getName());
            preparedStatement = connection.prepareStatement(daoWrapper.getSql());

            if (!StringUtil.notEmpty(daoWrapper.getResultType())){
                daoWrapper.setResultType(entityClass);
            }

            if(TYPE_SELECT.equals(daoWrapper.getType())){

                Class clazz = Class.forName(daoWrapper.getResultType());
                List list = new ArrayList();
                if (args != null){
                    for (int i = 0; i < args.length; i++) {
                        preparedStatement.setObject(i+1,args[i]);
                    }
                }

                resultSet = preparedStatement.executeQuery();

                Field[] declaredFields = clazz.getDeclaredFields();

                while (resultSet.next()) {
                    Object o = clazz.newInstance();
                    for (Field declaredField : declaredFields) {
                        declaredField.setAccessible(true);
                        declaredField.set(o,resultSet.getObject(StringUtil.toUnderScoreCase(declaredField.getName())));
                    }
                    list.add(o);
                }

                return list;
            } else if(TYPE_INSERT.equals(daoWrapper.getType()) ){
                //假定有且只有一个对象要操作,并且放到第一个
                Class clazz = args[0].getClass();
                Field[] declaredFields = clazz.getDeclaredFields();
                for (int i = 0; i < declaredFields.length; i++) {
                    declaredFields[i].setAccessible(true);
                    preparedStatement.setObject(i+1,declaredFields[i].get(args[0]));
                }

            }else if (TYPE_UPDATE.equals(daoWrapper.getType())){
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i+1,args[i]);
                }
            }
            else if(TYPE_DELETE.equals(daoWrapper.getType())){
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i+1,args[i]);
                }
            }
            preparedStatement.execute();

            if (dataSource != null){ connection.commit();}

        } catch (Exception e){
            System.out.println( method.getDeclaringClass().getName() +"的"+method.getName() +"----发生异常");
            if(dataSource != null){ connection.rollback();}
            e.printStackTrace();
        } finally {
            if (dataSource != null){
                JdbcUtil.close(connection,preparedStatement,resultSet);
            } else {
                JdbcUtil.close(preparedStatement,resultSet);
            }
        }

        return null;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {

            if (dataSource != null){
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
            }

            if (method.getDeclaringClass() == BaseDao.class){
                BaseDaoImpl baseDao = new BaseDaoImpl(connection,Class.forName(entityClass));
                Object obj =  method.invoke(baseDao,objects);
                connection.commit();
                return obj;
            }

            DaoWrapper daoWrapper = wapper.get(method.getName());
            preparedStatement = connection.prepareStatement(daoWrapper.getSql());

            if (!StringUtil.notEmpty(daoWrapper.getResultType())){
                daoWrapper.setResultType(entityClass);
            }

            if(TYPE_SELECT.equals(daoWrapper.getType())){

                Class clazz = Class.forName(daoWrapper.getResultType());
                List list = new ArrayList();
                if (objects != null){
                    for (int i = 0; i < objects.length; i++) {
                        preparedStatement.setObject(i+1,objects[i]);
                    }
                }

                resultSet = preparedStatement.executeQuery();

                Field[] declaredFields = clazz.getDeclaredFields();

                while (resultSet.next()) {
                    Object obj = clazz.newInstance();
                    for (Field declaredField : declaredFields) {
                        declaredField.setAccessible(true);
                        declaredField.set(obj,resultSet.getObject(StringUtil.toUnderScoreCase(declaredField.getName())));
                    }
                    list.add(obj);
                }
                return list;
            } else if(TYPE_INSERT.equals(daoWrapper.getType()) ){
                //假定有且只有一个对象要操作,并且放到第一个
                Class clazz = objects[0].getClass();
                Field[] declaredFields = clazz.getDeclaredFields();
                for (int i = 0; i < declaredFields.length; i++) {
                    declaredFields[i].setAccessible(true);
                    preparedStatement.setObject(i+1,declaredFields[i].get(objects[0]));
                }

            }else if (TYPE_UPDATE.equals(daoWrapper.getType())){
                for (int i = 0; i < objects.length; i++) {
                    preparedStatement.setObject(i+1,objects[i]);
                }
            }
            else if(TYPE_DELETE.equals(daoWrapper.getType())){
                for (int i = 0; i < objects.length; i++) {
                    preparedStatement.setObject(i+1,objects[i]);
                }
            }
            preparedStatement.execute();

        } catch (Exception e){
            System.out.println( method.getDeclaringClass().getName() +"的"+method.getName() +"----发生异常");
            if(dataSource != null){ connection.rollback();}
            e.printStackTrace();
        } finally {
            if (dataSource != null){
                connection.commit();
                JdbcUtil.close(connection,preparedStatement,resultSet);
            } else {
                JdbcUtil.close(preparedStatement,resultSet);
            }
        }

        return null;
    }
}
