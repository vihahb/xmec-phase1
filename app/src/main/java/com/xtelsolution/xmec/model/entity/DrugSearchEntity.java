package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public class DrugSearchEntity implements Serializable {

    @Expose
    public int id;
    @Expose
    public String drugName;
    @Expose
    public String productionCompany;
    @Expose
    public String drugType;
    @Expose
    public String drugThumb;
    @Expose
    public String drugFullimg;

    public DrugSearchEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getDrugThumb() {
        return drugThumb;
    }

    public void setDrugThumb(String drugThumb) {
        this.drugThumb = drugThumb;
    }

    public String getDrugFullimg() {
        return drugFullimg;
    }

    public void setDrugFullimg(String drugFullimg) {
        this.drugFullimg = drugFullimg;
    }

    @Override
    public String toString() {
        return "DrugSearchEntity{" +
                "id=" + id +
                ", drugName='" + drugName + '\'' +
                ", productionCompany='" + productionCompany + '\'' +
                ", drugType='" + drugType + '\'' +
                ", drugThumb='" + drugThumb + '\'' +
                ", drugFullimg='" + drugFullimg + '\'' +
                '}';
    }
}
