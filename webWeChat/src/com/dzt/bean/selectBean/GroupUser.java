package com.dzt.bean.selectBean;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Z
 */
public class GroupUser {
    private Integer userId;
    private String vest;
    private String name;
    private String profile;
    private Integer power;


    public GroupUser() {
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getVest() {
        return vest;
    }

    public void setVest(String vest) {
        this.vest = vest;
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

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "GroupUser{" +
                "userId=" + userId +
                ", vest='" + vest + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", power=" + power +
                '}';
    }
}
