package com.dzt.entity;

import com.alibaba.fastjson.JSON;
import com.dzt.core.DaoFacory;
import com.dzt.dao.MessageDao;

import java.sql.Timestamp;
import java.util.Date;

public class Message {

    private Integer id;
    private Integer fromId;
    private Integer toId;
    /**
     * 1,人对人
     * 2，人对群
     * 3，申请好友
     * 4，申请入群
     * 5，系统对人的消息（你被封号了，申请被拒绝）
     * 6, 发送举报消息
     * 7，@消息
     * 9, 弹出消息
     * 10, 群权限更新
     * 11， 好友权限更新
     * 12 朋友刷新
     * 13 群刷新
     * 14 群公告消息
     */
    private Integer msgType;

    private String content;
    /**
     * 消息的时间
     */
    private Timestamp time;

    /**
     * 当type是1，2的时候
     * status 0 是普通消息
     * 1 文件 content 就是文件的url
     */
    private Integer status;
    private String fileName;
    public Message() {}



    public Message(Integer fromId, Integer toId, Integer msgType, String content) {
        this.id = 0;
        this.fromId = fromId;
        this.toId = toId;
        this.msgType = msgType;
        this.content = content;
        Date date = new Date();
        this.time = new Timestamp(date.getTime());
        this.status = 0;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Message(Integer msgType) {
        this.msgType = msgType;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", msgType=" + msgType +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", status=" + status +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
