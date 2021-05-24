package com.dzt.entity;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Z
 */
public class PersonRelationship {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String remark;
    private Integer black;

    public PersonRelationship() {
    }

    public PersonRelationship( Integer fromId, Integer toId, String remark, Integer black) {
        this.id = 0;
        this.fromId = fromId;
        this.toId = toId;
        this.remark = remark;
        this.black = black;
    }

    public PersonRelationship(Integer fromId, Integer toId) {
        id = 0;
        this.fromId = fromId;
        this.toId = toId;
        this.remark = "";
    }

    public Integer getBlack() {
        return black;
    }

    public void setBlack(Integer black) {
        this.black = black;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PersonRelationship{" +
                "id=" + id +
                ", from=" + fromId +
                ", to=" + toId +
                ", remark='" + remark + '\'' +
                '}';
    }
}
