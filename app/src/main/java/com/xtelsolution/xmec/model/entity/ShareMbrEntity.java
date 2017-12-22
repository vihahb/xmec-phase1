package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by xtel on 11/14/17.
 */

public class ShareMbrEntity implements Serializable {
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
    @Expose
    private int shareId;
    /**
     * 1 - Chia sẻ
     * 2 - Liên kết
     */
    @Expose
    private int shareType;

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
    /**
     * 0 - Pending
     * 1 - Đồng ý
     * 2 - Từ chối
     */
    @Expose
    private int shareState;

    @Expose
    private String shareDate;
    @Expose
    private Long shareDateLong;

    public ShareMbrEntity() {
    }

    public ShareMbrEntity(ShareAccounts shareAccounts) {
        this.uid = shareAccounts.getUid();
        this.fullname = shareAccounts.getFullname();
        this.avatar = shareAccounts.getAvatar();
        this.shareId = shareAccounts.getId();
        this.shareType = shareAccounts.getShareType();
        this.shareTo = shareAccounts.getShareType();
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

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public int getShareTo() {
        return shareTo;
    }

    public void setShareTo(int shareTo) {
        this.shareTo = shareTo;
    }

    public int getShareState() {
        return shareState;
    }

    public void setShareState(int shareState) {
        this.shareState = shareState;
    }

    public int getShareId() {
        return shareId;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public String getShareDate() {
        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public Long getShareDateLong() {
        return shareDateLong;
    }

    public void setShareDateLong(Long shareDateLong) {
        this.shareDateLong = shareDateLong;
    }

    @Override
    public String toString() {
        return "ShareMbrEntity{" +
                "uid=" + uid +
                ", fullname='" + fullname + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", birthdayLong=" + birthdayLong +
                ", phonenumber='" + phonenumber + '\'' +
                ", address='" + address + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", avatar='" + avatar + '\'' +
                ", shareId=" + shareId +
                ", shareType=" + shareType +
                ", shareTo=" + shareTo +
                ", shareState=" + shareState +
                ", shareDate='" + shareDate + '\'' +
                ", shareDateLong=" + shareDateLong +
                '}';
    }
}
