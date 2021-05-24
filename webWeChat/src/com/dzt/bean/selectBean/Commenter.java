package com.dzt.bean.selectBean;

/**
 * @author Z
 */

/**
 * 用户评论的包装
 */
public class Commenter {
    private Integer id;
    private Integer userId;
    private String userName;
    private String profile;
    private Integer momentId;
    private String content;
    private Integer status;
    private Integer upId;
    private Integer upUserId;
    private String upUserName;


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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getMomentId() {
        return momentId;
    }

    public void setMomentId(Integer momentId) {
        this.momentId = momentId;
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

    public Integer getUpId() {
        return upId;
    }

    public void setUpId(Integer upId) {
        this.upId = upId;
    }

    public Integer getUpUserId() {
        return upUserId;
    }

    public void setUpUserId(Integer upUserId) {
        this.upUserId = upUserId;
    }

    public String getUpUserName() {
        return upUserName;
    }

    public void setUpUserName(String upUserName) {
        this.upUserName = upUserName;
    }

    @Override
    public String toString() {
        return "Commenter{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", profile='" + profile + '\'' +
                ", momentId=" + momentId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", upId=" + upId +
                ", upUserId=" + upUserId +
                ", upUserName='" + upUserName + '\'' +
                '}';
    }
}
