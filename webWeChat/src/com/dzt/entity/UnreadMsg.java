package com.dzt.entity;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Z
 */
public class UnreadMsg {
    private Integer id;
    private Integer msgId;
    private Integer userId;


    public UnreadMsg() {
    }

    public UnreadMsg(Integer msgId, Integer userId) {
        this.id = 0;
        this.msgId = msgId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
