package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.resp.RESP_Friend;

import java.io.Serializable;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class Friends implements Serializable {
    private int id;
    @Expose
    private int uid;
    @Expose
    private String fullname;
    @Expose
    private int gender;
    @Expose
    private String birthday;
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
    /**
     * Chia sẻ
     * 1 - Hiện tại
     * 2 - Tương lai
     * Liên kết
     * 1 - Chỉ xem
     * 2 - Xem và sửa
     */
    @Expose
    private int shareTo;

    public Friends() {
    }

    public Friends(ShareAccounts shareAccounts) {
        id = shareAccounts.getId();
        uid = shareAccounts.getUid();
        fullname = shareAccounts.getFullname();
        shareTo = shareAccounts.getShareTo();
        avatar = shareAccounts.getAvatar();
    }

    public Friends(RESP_Friend respFriend) {
        id = respFriend.getId();
        uid = respFriend.getUid();
        fullname = respFriend.getFullname();
        gender = respFriend.getGender();
        birthday = respFriend.getBirthday();
        birthdayLong = respFriend.getBirthdayLong();
        phonenumber = respFriend.getPhonenumber();
        address = respFriend.getAddress();
        weight = respFriend.getWeight();
        height = respFriend.getHeight();
        avatar = respFriend.getAvatar();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public int getShareTo() {
        return shareTo;
    }

    public void setShareTo(int shareTo) {
        this.shareTo = shareTo;
    }
}