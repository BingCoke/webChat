package com.dzt.bean.selectBean;

import java.sql.Timestamp;

/**
 * @author Z
 */
public class Publisher {
    private Integer userId;
    private Integer id;
    private String content;
    private String remark;
    private Timestamp time;
    private String profile;

    public Publisher() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "userId=" + userId +
                ", id=" + id +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                ", time=" + time +
                ", profile='" + profile + '\'' +
                '}';
    }
}
