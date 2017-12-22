package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vivu on 11/28/17
 * xtel.vn
 */

public class SelfRateObject implements Serializable{
    @Expose
    public int id;
    @Expose
    public String dateCreated;
    @Expose
    public long dateCreatedLong;
    @Expose
    public int qualityRate;
    @Expose
    public int hygieneRate;
    @Expose
    public int serviceRate;
    @Expose
    public String comment;
    @Expose
    public int uid;
    @Expose
    public int hospitalId;
    @Expose
    public String fullname;
    @Expose
    public String avatar;

    public SelfRateObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateCreatedLong() {
        return dateCreatedLong;
    }

    public void setDateCreatedLong(long dateCreatedLong) {
        this.dateCreatedLong = dateCreatedLong;
    }

    public int getQualityRate() {
        return qualityRate;
    }

    public void setQualityRate(int qualityRate) {
        this.qualityRate = qualityRate;
    }

    public int getHygieneRate() {
        return hygieneRate;
    }

    public void setHygieneRate(int hygieneRate) {
        this.hygieneRate = hygieneRate;
    }

    public int getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(int serviceRate) {
        this.serviceRate = serviceRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "SelfRateObject{" +
                "id=" + id +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateCreatedLong='" + dateCreatedLong + '\'' +
                ", qualityRate=" + qualityRate +
                ", hygieneRate=" + hygieneRate +
                ", serviceRate=" + serviceRate +
                ", comment='" + comment + '\'' +
                ", uid=" + uid +
                ", hospitalId=" + hospitalId +
                ", fullname='" + fullname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
