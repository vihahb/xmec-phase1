package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class UserDrugs extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
    private int id;
    @Expose
    private String drugName;
    @Expose
    private int drugId;
    @Expose
    private String drugType;
    @Expose
    private String note;
    @Expose
    private int healthRecordId;
    @Expose
//    @Ignore
    private RealmList<UserDrugImages> userDrugImages;

    public UserDrugs() {
        id = -1;
        drugId = -1;
        healthRecordId = -1;
        userDrugImages = new RealmList<>();
    }

    public UserDrugs(int id, String drugName, int drugId, String drugType, String note, int healthRecordId, RealmList<UserDrugImages> userDrugImages) {
        this.id = id;
        this.drugName = drugName;
        this.drugId = drugId;
        this.drugType = drugType;
        this.note = note;
        this.healthRecordId = healthRecordId;
        this.userDrugImages = userDrugImages;
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

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(int healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public RealmList<UserDrugImages> getUserDrugImages() {
        return userDrugImages;
    }

    public void setUserDrugImages(RealmList<UserDrugImages> userDrugImages) {
        this.userDrugImages = userDrugImages;
    }
}