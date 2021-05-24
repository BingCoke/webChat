package com.dzt.bean.selectBean;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Z
 * 好友的包装
 */
public class MyFriend {
    private Integer id;
    private Integer userId;
    private String profile;
    private String remark;
    private String name;
    /**
     * 0 是没有拉黑
     * 1 拉黑了
     */
    private Integer black;

    public MyFriend() {
    }

    public Integer getBlack() {
        return black;
    }

    public void setBlack(Integer black) {
        this.black = black;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "MyFriend{" +
                "id=" + id +
                ", userId=" + userId +
                ", profile='" + profile + '\'' +
                ", remark='" + remark + '\'' +
                ", name='" + name + '\'' +
                ", black=" + black +
                '}';
    }
}
