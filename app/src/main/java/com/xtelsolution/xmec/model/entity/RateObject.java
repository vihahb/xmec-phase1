package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by vivu on 11/29/17
 * xtel.vn
 */

public class RateObject implements Serializable {

    @Expose
    public int qualityRate;
    @Expose
    public int hygieneRate;
    @Expose
    public int serviceRate;
    @Expose
    public String comment;
    @Expose
    public int hospitalId;
    @Expose
    private int id;

    public RateObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public String toString() {
        return "RateObject{" +
                "id=" + id +
                ", qualityRate=" + qualityRate +
                ", hygieneRate=" + hygieneRate +
                ", serviceRate=" + serviceRate +
                ", comment='" + comment + '\'' +
                ", hospitalId=" + hospitalId +
                '}';
    }
}
