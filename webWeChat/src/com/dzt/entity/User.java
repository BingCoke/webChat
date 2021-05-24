package com.dzt.entity;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.Objects;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String phone;
    private String profile;
    private String mail;
    /**
     * 0 封号
     * 1 正常用户
     * 2 游客
     */
    private Integer power;
    /**
     * 聊天背景
     *
     */
    private String back;

    public User() {
    }

    public User(Integer power) {
        this.power = power;
    }

    public User(String username, String password, String name, String gender, String phone, String profile, String mail) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.profile = profile;
        this.mail = mail;
    }

    public User(String username,String name,Integer power) {
        this.username = username;
        this.name = name;
        this.power = power;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", profile='" + profile + '\'' +
                ", mail='" + mail + '\'' +
                ", power=" + power +
                ", back='" + back + '\'' +
                '}';
    }
}
