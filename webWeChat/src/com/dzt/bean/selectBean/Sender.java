package com.dzt.bean.selectBean;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;

/**
 * @author Z
 */

public class Sender {
    private Integer userId;
    private String name;
    private String profile;
    private Integer msgId;
    private Integer msgType;
    private Integer toId;
    private Timestamp time;
    private String content;

    public Sender() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
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

    @Override
    public String toString() {
        return "Sender{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", msgId=" + msgId +
                ", msgType=" + msgType +
                ", toId=" + toId +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
