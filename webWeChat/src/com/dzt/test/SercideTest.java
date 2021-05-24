package com.dzt.test;

import com.alibaba.fastjson.JSONObject;
import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.Commenter;
import com.dzt.bean.selectBean.IntegerCount;
import com.dzt.bean.selectBean.LongCount;
import com.dzt.core.BeanFactory;
import com.dzt.core.DaoFacory;
import com.dzt.core.DataSourceFactory;
import com.dzt.core.ToJSON;
import com.dzt.dao.*;
import com.dzt.entity.*;
import com.dzt.service.AdminService;
import com.dzt.service.MessageService;
import com.dzt.service.UserGroupService;
import com.dzt.service.UserService;
import com.dzt.service.impl.AdminServiceImpl;
import com.dzt.service.impl.MessageServiceImpl;
import com.dzt.service.impl.UserGroupServiceImpl;
import com.dzt.service.impl.UserServiceImpl;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SercideTest {
    @Test
    public void test1(){

        UserService userService = BeanFactory.getBean(UserServiceImpl.class);
        System.out.println(userService);
        System.out.println(userService.hasUsername("dffg"));
    }


    @Test
    public void test2(){
        UserDao userDao = DaoFacory.getWebChatDao(UserDao.class);
        User user = userDao.add(new User("asdf","asdf","fff","s","fff","fff","sdf"));
        System.out.println(user);
    }
    @Test
    public void test3(){

        String r = MyResult.build().setCode(233).setMsg("dddd").toJson().toString();
        JSONObject jsonObject = (JSONObject) JSONObject.parse(r);
        System.out.println(jsonObject.get("code").getClass().getName());
        System.out.println(jsonObject.get("msg"));
    }


    @Test
    public void test4() throws SQLException {
        DataSource dataSource = DataSourceFactory.creatDataSource(DataSourceFactory.WEB_DATASOURCE);
            Connection connection  = dataSource.getConnection();

            new Thread(new Task(dataSource)).start();




    }

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = DataSourceFactory.creatDataSource(DataSourceFactory.WEB_DATASOURCE);

        new Thread(new Task(dataSource)).start();
        new Thread(new Task(dataSource)).start();


    }


    public static class Task implements Runnable {
        DataSource dataSource;
        public Task(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void run() {

            Connection connection  = null;
            Connection connection1  = null;
            Connection connection3  = null;
            try {
                connection  = dataSource.getConnection();
            } catch (SQLException  throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    @Test
    public void test6(){
        MessageService messageService = BeanFactory.getBean(MessageServiceImpl.class);
        MessageDao messageDao = DaoFacory.getWebChatDao(MessageDao.class);
        System.out.println(messageDao.findSameFriendApplication(2, 11).get(0).getCount());

    }


    @Test
    public void test7(){
        PersonRelationshipDao personRelationshipDao = DaoFacory.getWebChatDao(PersonRelationshipDao.class);
        personRelationshipDao.add(new PersonRelationship(2,11));
    }

    @Test
    public void test8(){
        UserDao userDao = DaoFacory.getWebChatDao(UserDao.class);
        System.out.println(userDao.getMyFriend(17));
    }


    @Test
    public void groupDaptTest(){
        UserGroupService userGroupService = BeanFactory.getBean(UserGroupServiceImpl.class);
        UserGroupDao userGroupDao = DaoFacory.getWebChatDao(UserGroupDao.class);
        System.out.println(userGroupDao.selectById(5));
    }

    @Test
    public void memberTest(){
        GroupMemberDao groupMemberDao = DaoFacory.getWebChatDao(GroupMemberDao.class);
        List<GroupMember> management = groupMemberDao.getManagement(5);
        System.out.println(management);
    }

    @Test
    public void test9() throws NoSuchMethodException {
        Class clazz = com.dzt.service.impl.UserGroupServiceImpl.class;
        Method method = clazz.getMethod("toCreat", new Class[]{HttpServletRequest.class, HttpServletResponse.class});
        System.out.println(method.getName());
    }


    @Test
    public void test10(){
         MessageDao messageDao = DaoFacory.getWebChatDao(MessageDao.class);
        System.out.println(messageDao.getGroupApplicationMsg(17));
    }

    @Test
    public void test11(){
        MessageDao messageDao = DaoFacory.getWebChatDao(MessageDao.class);
        System.out.println(messageDao.getFriendApplicationMsg(21));
    }


    @Test
    public void test12(){
        GroupMemberDao groupMemberDao = DaoFacory.getWebChatDao(GroupMemberDao.class);
        System.out.println(groupMemberDao.getGroupMember(5));
    }

    @Test
    public void test13(){
        GroupMemberDao groupMemberDao = DaoFacory.getWebChatDao(GroupMemberDao.class);
        groupMemberDao.saveVest("hhh",5,17);
    }

    @Test
    public void test14(){


    }

    @Test
    public void test19(){
        Integer l = 3;
        Integer m = 2 + l;
        System.out.println(m);
    }

    @Test
    public void test21(){
        MomentDao momentDao = DaoFacory.getWebChatDao(MomentDao.class);
        System.out.println(momentDao.getFriendsMoment(17, 17, 17, 0, 10));
        System.out.println(momentDao.getFriendsMoment(17, 17, 17, 0, 10));
        System.out.println(momentDao.getFriendsMoment(17, 17, 17, 0, 10));
        System.out.println(momentDao.getFriendsMoment(17, 17, 17, 0, 10));

    }

    @Test
    public void teste22(){
        CommentDao commentDao = DaoFacory.getWebChatDao(CommentDao.class);
        List<Commenter> momentComment = commentDao.getMomentComment(26);
        System.out.println(momentComment);
        System.out.println(MyResult.build().setData(momentComment).toJson());
    }

    @Test
    public void tetst32(){
        AdminService adminService = BeanFactory.getBean(AdminServiceImpl.class);
        System.out.println(adminService.login("123", "123qwe"));
    }

    @Test
    public void test23(){
        System.out.println(MyResult.build().setData(new User(9)));
    }

    @Test
    public void test32(){
        MessageDao messageDao = DaoFacory.getWebChatDao(MessageDao.class);
        messageDao.getGroupMessage(5,0,10);
    }


}
