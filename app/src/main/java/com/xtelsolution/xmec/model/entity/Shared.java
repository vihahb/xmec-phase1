package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lecon on 12/4/2017
 */

public class Shared extends RealmObject {
    @Expose
    @PrimaryKey
    private int mrbId;
    @Expose
    private int createUid;
    @Expose
    private String name;
    @Expose
    private long birthdayLong;
    @Expose
    private int gender;
    @Expose
    private int relationshipType;
    @Expose
    private double height;
    @Expose
    private double weight;
    @Expose
    private int bloodType;
    @Expose
    private String creatorAvatar;
    @Expose
    private String creatorName;
    @Expose
    private String avatar;
    @Expose
    private long shareDateLong;
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
    @Expose
    private RealmList<ShareAccounts> shareAccounts;
    @Expose
    private RealmList<HealthRecords> healthRecords;

    public Shared() {
    }

    public int getMrbId() {
        return mrbId;
    }

    public void setMrbId(int mrbId) {
        this.mrbId = mrbId;
    }

    public int getCreateUid() {
        return createUid;
    }

    public void setCreateUid(int createUid) {
        this.createUid = createUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthdayLong() {
        return birthdayLong;
    }

    public void setBirthdayLong(long birthdayLong) {
        this.birthdayLong = birthdayLong;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getBloodType() {
        return bloodType;
    }

    public void setBloodType(int bloodType) {
        this.bloodType = bloodType;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getShareDateLong() {
        return shareDateLong;
    }

    public void setShareDateLong(long shareDateLong) {
        this.shareDateLong = shareDateLong;
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

    public RealmList<ShareAccounts> getShareAccounts() {
        return shareAccounts;
    }

    public void setShareAccounts(RealmList<ShareAccounts> shareAccounts) {
        this.shareAccounts = shareAccounts;
    }

    public RealmList<HealthRecords> getHealthRecords() {
        return healthRecords;
    }

    public void setHealthRecords(RealmList<HealthRecords> healthRecords) {
        this.healthRecords = healthRecords;
    }
}