package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Created by lecon on 11/27/2017
 */

public class REQ_User {
    @Expose
    private Integer uid;
    @Expose
    private String fullname;
    @Expose
    private Integer gender;
    @Expose
    private Long birthdayLong;
    @Expose
    private String phonenumber;
    @Expose
    private String address;
    @Expose
    private Double weight;
    @Expose
    private Double height;
    @Expose
    private String avatar;

    public REQ_User() {
    }

    public REQ_User(Integer uid, String fullname, Integer gender, Long birthdayLong, String phonenumber, String address, Double weight, Double height, String avatar) {
        this.uid = uid;
        this.fullname = fullname;
        this.gender = gender;
        this.birthdayLong = birthdayLong;
        this.phonenumber = phonenumber;
        this.address = address;
        this.weight = weight;
        this.height = height;
        this.avatar = avatar;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getBirthdayLong() {
        return birthdayLong;
    }

    public void setBirthdayLong(Long birthdayLong) {
        this.birthdayLong = birthdayLong;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}