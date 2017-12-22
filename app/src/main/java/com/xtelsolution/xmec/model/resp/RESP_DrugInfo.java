package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public class RESP_DrugInfo extends RESP_Basic{
    @Expose
    public int id;

    //Tên thuốc
    @Expose
    public String drugName;
    //Công ty sản xuất
    @Expose
    public String productionCompany;

    //Loại thuốc
    @Expose
    public String drugType;

    //Ảnh xem trước
    @Expose
    public String drugThumb;

    //Full Image
    @Expose
    public String drugFullimg;

    //Tổng quan
    @Expose
    public String drugSummary;

    //Cách Dùng
    @Expose
    public String drugUsage;

    //Thông tin
    @Expose
    public String drugInfo;

    //Cảnh báo
    @Expose
    public String drugWarn;

    //Chỉ định
    @Expose
    public String drugPoint;

    //Chống chỉ định
    @Expose
    public String drugUnpoint;

    //Tác dụng phụ
    @Expose
    public String drugSideEffect;

    //Lưu ý
    @Expose
    public String drugNote;

    //Quá liệuf
    @Expose
    public String drugOverdose;

    //Bảo quản
    @Expose
    public String drugPreservation;

    //Nếu quên uống thuốc
    public String drugForget;

    //Chế độ ăn uống
    @Expose
    public String drugDiet;

    //Tương tác
    @Expose
    public String drugInteractive;

    //Cơ chế
    @Expose
    public String drugMechanism;

    //Dược động học
    @Expose
    public String drugKinetics;

    public RESP_DrugInfo() {
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

    public String getDrugSummary() {
        return drugSummary;
    }

    public void setDrugSummary(String drugSummary) {
        this.drugSummary = drugSummary;
    }

    public String getDrugUsage() {
        return drugUsage;
    }

    public void setDrugUsage(String drugUsage) {
        this.drugUsage = drugUsage;
    }

    public String getDrugInfo() {
        return drugInfo;
    }

    public void setDrugInfo(String drugInfo) {
        this.drugInfo = drugInfo;
    }

    public String getDrugWarn() {
        return drugWarn;
    }

    public void setDrugWarn(String drugWarn) {
        this.drugWarn = drugWarn;
    }

    public String getDrugPoint() {
        return drugPoint;
    }

    public void setDrugPoint(String drugPoint) {
        this.drugPoint = drugPoint;
    }

    public String getDrugUnpoint() {
        return drugUnpoint;
    }

    public void setDrugUnpoint(String drugUnpoint) {
        this.drugUnpoint = drugUnpoint;
    }

    public String getDrugSideEffect() {
        return drugSideEffect;
    }

    public void setDrugSideEffect(String drugSideEffect) {
        this.drugSideEffect = drugSideEffect;
    }

    public String getDrugNote() {
        return drugNote;
    }

    public void setDrugNote(String drugNote) {
        this.drugNote = drugNote;
    }

    public String getDrugOverdose() {
        return drugOverdose;
    }

    public void setDrugOverdose(String drugOverdose) {
        this.drugOverdose = drugOverdose;
    }

    public String getDrugPreservation() {
        return drugPreservation;
    }

    public void setDrugPreservation(String drugPreservation) {
        this.drugPreservation = drugPreservation;
    }

    public String getDrugForget() {
        return drugForget;
    }

    public void setDrugForget(String drugForget) {
        this.drugForget = drugForget;
    }

    public String getDrugDiet() {
        return drugDiet;
    }

    public void setDrugDiet(String drugDiet) {
        this.drugDiet = drugDiet;
    }

    public String getDrugInteractive() {
        return drugInteractive;
    }

    public void setDrugInteractive(String drugInteractive) {
        this.drugInteractive = drugInteractive;
    }

    public String getDrugMechanism() {
        return drugMechanism;
    }

    public void setDrugMechanism(String drugMechanism) {
        this.drugMechanism = drugMechanism;
    }

    public String getDrugKinetics() {
        return drugKinetics;
    }

    public void setDrugKinetics(String drugKinetics) {
        this.drugKinetics = drugKinetics;
    }

    @Override
    public String toString() {
        return "RESP_DrugInfo{" +
                "id=" + id +
                ", drugName='" + drugName + '\'' +
                ", productionCompany='" + productionCompany + '\'' +
                ", drugType='" + drugType + '\'' +
                ", drugThumb='" + drugThumb + '\'' +
                ", drugFullimg='" + drugFullimg + '\'' +
                ", drugSummary='" + drugSummary + '\'' +
                ", drugUsage='" + drugUsage + '\'' +
                ", drugInfo='" + drugInfo + '\'' +
                ", drugWarn='" + drugWarn + '\'' +
                ", drugPoint='" + drugPoint + '\'' +
                ", drugUnpoint='" + drugUnpoint + '\'' +
                ", drugSideEffect='" + drugSideEffect + '\'' +
                ", drugNote='" + drugNote + '\'' +
                ", drugOverdose='" + drugOverdose + '\'' +
                ", drugPreservation='" + drugPreservation + '\'' +
                ", drugForget='" + drugForget + '\'' +
                ", drugDiet='" + drugDiet + '\'' +
                ", drugInteractive='" + drugInteractive + '\'' +
                ", drugMechanism='" + drugMechanism + '\'' +
                ", drugKinetics='" + drugKinetics + '\'' +
                '}';
    }
}
