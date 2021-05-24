package com.dzt.entity;

/**
 * @author Z
 */
public class Comment {
    private Integer id;
    private String content;
    private Integer momentId;
    private Integer userId;
    private Integer upId;
    /**
     * 0 没有上级评论
     * 1 有上级评论
     */
    private Integer status;


    public Comment() {
    }


    public Comment(String content, Integer momentId, Integer userId, Integer upId) {
        this.id = 0;
        this.content = content;
        this.userId = userId;
        this.upId = upId;
        this.status = 1;
        this.momentId = momentId;
    }

    public Comment(String content, Integer momentId, Integer userId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.status = 0;
        this.momentId = momentId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUpId() {
        return upId;
    }

    public void setUpId(Integer upId) {
        this.upId = upId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMomentId() {
        return momentId;
    }

    public void setMomentId(Integer momentId) {
        this.momentId = momentId;
    }
}
