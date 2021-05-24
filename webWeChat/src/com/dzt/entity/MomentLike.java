package com.dzt.entity;

import javax.mail.internet.InternetAddress;
import java.awt.print.PrinterGraphics;

/**
 * @author Z
 */
public class MomentLike {
    private Integer id;
    private Integer momentId;
    private Integer userId;

    public MomentLike() {
    }

    public MomentLike(Integer momentId, Integer userId) {
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
