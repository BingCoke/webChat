package com.dzt.entity;

/**
 * @author Z
 */
public class UnLook {
    private Integer id;
    private Integer momentId;
    private Integer userId;

    public UnLook() {
    }

    public UnLook(Integer momentId, Integer userId) {
        this.id = 0;
        this.momentId = momentId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMomentId() {
        return momentId;
    }

    public void setMomentId(Integer momentId) {
        this.momentId = momentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
