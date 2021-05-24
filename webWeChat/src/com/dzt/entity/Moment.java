package com.dzt.entity;

import javax.mail.internet.InternetAddress;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Z
 */
public class Moment {
    private Integer id;
    private String content;
    private Timestamp time;
    private Integer userId;

    public Moment() {
    }

    public Moment(String content, Integer userId) {
        this.id = 0;
        this.content = content;
        this.time = new Timestamp(System.currentTimeMillis());
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
