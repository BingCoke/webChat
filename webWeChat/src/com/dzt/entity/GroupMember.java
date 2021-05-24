package com.dzt.entity;

/**
 * @author Z
 */
public class GroupMember {
    private Integer id;
    private Integer groupId;
    private Integer userId;
    private Integer power;
    private String vest;

    public GroupMember() {
    }

    public GroupMember(Integer groupId, Integer userId, Integer power, String vest) {
        this.id = 0;
        this.groupId = groupId;
        this.userId = userId;
        this.power = power;
        this.vest = vest;
    }
    public Integer getId() {
        return id;
    }

    public String getVest() {
        return vest;
    }
    public void setVest(String vest) {
        this.vest = vest;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }


    @Override
    public String toString() {
        return "GroupMember{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", power=" + power +
                ", vest='" + vest + '\'' +
                '}';
    }
}
