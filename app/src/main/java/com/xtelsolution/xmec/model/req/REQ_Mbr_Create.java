package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Mbr_Create {
    @Expose
    private String name;
    @Expose
    private int gender;
    @Expose
    private long birthdayLong;
    @Expose
    private int relationshipType;
    @Expose
    private String avatar;

    public REQ_Mbr_Create() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getBirthdayLong() {
        return birthdayLong;
    }

    public void setBirthdayLong(long birthdayLong) {
        this.birthdayLong = birthdayLong;
    }

    public int getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}