package com.dzt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dzt.bean.MyMail;
import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.MyFriend;
import com.dzt.controller.message.Msg;
import com.dzt.core.BeanFactory;
import com.dzt.core.DaoFacory;
import com.dzt.dao.GroupMemberDao;
import com.dzt.dao.UserDao;
import com.dzt.dao.UserGroupDao;
import com.dzt.entity.GroupMember;
import com.dzt.entity.Message;
import com.dzt.entity.User;
import com.dzt.entity.UserGroup;
import com.dzt.service.PersonRelationshipService;
import com.dzt.service.UserService;
import com.dzt.util.PatternUtils;
import com.dzt.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Part;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Z
 */
public class UserServiceImpl implements UserService {
    private final UserDao userDao = DaoFacory.getWebChatDao(UserDao.class);
    private PersonRelationshipService personRelationshipService = BeanFactory.getBean(PersonRelationShipServiceImpl.class);
    @Override
    public void register(User user) {
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        userDao.add(user);
    }

    @Override
    public Boolean hasUsername(String username) {
        List<User> users = userDao.selectExact("username",username);
        if(users.size() == 0){
            return false;
        }
        return true;
    }

    @Override
    public List login(String username, String password) {
        List<User> users = userDao.selectExact("username", username);
        List list = new ArrayList();
        if (!StringUtil.notEmpty(username) || !StringUtil.notEmpty(password)){
            list.add(MyResult.build().setCode(300).setMsg("填写完整信息!").toJson());
            return list;
        }
        if (users.size() != 0){
            User user = users.get(0);
            if (user.getPassword().equals(DigestUtils.md5Hex(password))){
                list.add(MyResult.build().setCode(200).setMsg("成功").add("data",JSONObject.toJSON(user)).toJson());
                list.add(user);
                return list;
            }
        }
        list.add(MyResult.build().setCode(300).setMsg("账号或者密码错误").toJson());
        return list;
    }

    @Override
    public Boolean checkMail(String mail) {
        List<User> users = userDao.selectExact("mail",mail);
        if(users.size() == 0){
            return false;
        }
        return true;
    }

    @Override
    public String saveProfile(Part profile, String username) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String prePath = null;
        String fileName = null;
        try{
            //设置头像拷贝后的地址
            inputStream = profile.getInputStream();
            Calendar calendar = Calendar.getInstance();
            prePath = calendar.get(Calendar.YEAR) +"/"+ calendar.get(Calendar.MONTH) +"/";
            //保证文件名字不会重复
            fileName = UUID.randomUUID() +"_"+ profile.getSubmittedFileName();
            //创建文件夹
            File file = new File("Z:/img/"+ prePath);
            file.mkdirs();
            //把头像拷贝到指定的文件夹
            outputStream = new FileOutputStream( "Z:/img/"+ prePath + fileName);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,len);
            }
            userDao.saveProfile(prePath + fileName,username);
        } finally {
            inputStream.close();
            outputStream.close();
        }
        return prePath + fileName;
    }

    @Override
    public User selectByUsername(String username) {
        List<User> users = userDao.selectExact("username", username);
        User user = null;
        if (users.size() > 0){
            user = users.get(0);
        }
        return user;
    }

    @Override
    public String selectByUserId(int userId) {
        User user = userDao.selectById(userId);
        if (user != null){
            return MyResult.build().setCode(200).setMsg("成功").add("data",user).toJson();
        }
        return MyResult.build().setCode(300).setMsg("失败了").toJson();
    }

    @Override
    public String updateUserInformation(String name, String gender, String phone, int id) {
        if (name.length() > 12 || !PatternUtils.phoneCheck(phone)){
            return MyResult.build().setData(300).setMsg("请检查你的信息是否正确").toJson();
        } else {
            userDao.updateInformation(name,gender,phone,id);
            return MyResult.build().setData(200).setMsg("修改成功").toJson();
        }
    }

    @Override
    public String sendMailToUpdate(int userId) {
        User user = userDao.selectById(userId);
        String mail = user.getMail();
        String code = MyMail.getCode();
        MyMail.sendToOldMai(mail,code);
        return code;
    }

    @Override
    public String sendToNewMailCode(String newMail) {
        String code = MyMail.getCode();
        MyMail.sendToOldMai(newMail,code);
        return code;
    }

    @Override
    public void updateMail(String newMail, int userId) {
        userDao.updateMail(newMail, userId);
    }

    @Override
    public void updatePassword(String password, int userId) {
        userDao.updatePassword(DigestUtils.md5Hex(password),userId);
    }



    @Override
    public List<MyFriend> getMyFriends(int userId) {
        return userDao.getMyFriend(userId);
    }

    @Override
    public User getUserById(Integer toUserId) {
        return userDao.selectById(toUserId);
    }

    @Override
    public Map getGroupAuthoritiesMap(int id) {
        GroupMemberDao groupMemberDao = DaoFacory.getWebChatDao(GroupMemberDao.class);
        Map map = new HashMap();
        List<GroupMember> groupMembers = groupMemberDao.selectExact("userId", id);
        for (GroupMember groupMember : groupMembers) {
            map.put(String.valueOf(groupMember.getGroupId()),groupMember.getPower());
        }
        return map;
    }

    @Override
    public Map getFriendAuthoritiesMap(int id) {
        List<MyFriend> myFriends = getMyFriends(id);
        Map map = new HashMap();
        for (MyFriend myFriend : myFriends) {
            if (myFriend.getBlack() == 1) {
                map.put(String.valueOf(myFriend.getUserId()) ,1);
            } else {
                if (personRelationshipService.isBlack(id,myFriend.getUserId())) {
                    map.put(String.valueOf(myFriend.getUserId()),1);
                } else {
                    map.put(String.valueOf(myFriend.getUserId()),0);
                }
            }
        }
        return map;
    }

    @Override
    public String savaBack(Part part, User user) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String prePath = null;
        String fileName = null;
        try{

            inputStream = part.getInputStream();
            Calendar calendar = Calendar.getInstance();
            prePath = calendar.get(Calendar.YEAR) +"/"+ calendar.get(Calendar.MONTH) +"/";
            //保证文件名字不会重复
            fileName = UUID.randomUUID() +"_"+ part.getSubmittedFileName();
            //创建文件夹
            File file = new File("Z:/img/"+ prePath);
            file.mkdirs();
            //把头像拷贝到指定的文件夹
            outputStream = new FileOutputStream( "Z:/img/"+ prePath + fileName);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,len);
            }
            userDao.saveBack(prePath + fileName,user.getId());
        } finally {
            inputStream.close();
            outputStream.close();
        }
        return prePath + fileName;
    }

    @Override
    public String saveFile(Part part) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String prePath = null;
        String fileName = null;
        try{
            inputStream = part.getInputStream();
            Calendar calendar = Calendar.getInstance();
            prePath = calendar.get(Calendar.YEAR) +"/"+ calendar.get(Calendar.MONTH) +"/";
            //保证文件名字不会重复
            fileName = UUID.randomUUID() +"_"+ part.getSubmittedFileName();
            //创建文件夹
            File file = new File("Z:/file/"+ prePath);
            file.mkdirs();
            //把头像拷贝到指定的文件夹
            outputStream = new FileOutputStream( "Z:/file/"+ prePath + fileName);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,len);
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
        return prePath + fileName;
    }

    @Override
    public void seal(User user, int date) {
        userDao.updatePower(0,user.getId());
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        int userId = user.getId();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                userDao.updatePower(1,userId);
                ConcurrentHashMap<Integer, Msg> webSocketMap = Msg.getWebSocketMap();
                if (webSocketMap.containsKey(userId)){
                    Msg msg = webSocketMap.get(userId);
                    Message message = new Message(0,userId,10,"");
                    try {
                        msg.getSession().getBasicRemote().sendText(MyResult.build().add("msg",message).toJson());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };
        timer.schedule(runnable,date, TimeUnit.DAYS);
    }

    @Override
    public void removeSeal(User user) {
        userDao.updatePower(1,user.getId());
    }

    @Override
    public User add(User user) {
        return userDao.add(user);
    }


}
