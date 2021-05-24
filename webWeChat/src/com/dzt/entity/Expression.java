package com.dzt.entity;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Z
 */
public class Expression {
    private Integer id;
    private Integer userId;
    private String src;

    public Expression(Integer userId, String src) {
        this.userId = userId;
        this.src = src;
    }

    public Expression() {
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
