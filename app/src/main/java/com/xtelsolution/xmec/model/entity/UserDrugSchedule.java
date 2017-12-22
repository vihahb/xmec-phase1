package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/2/2017
 * Email: leconglongvu@gmail.com
 */
public class UserDrugSchedule extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
    private int id;
    @Expose
    private String drugName;
    @Expose
    private int amount;
    @Expose
    private int userDrugId;
    @Expose
    private int scheduleId;
    @Expose
    private int unit;

    public UserDrugSchedule() {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUserDrugId() {
        return userDrugId;
    }

    public void setUserDrugId(int userDrugId) {
        this.userDrugId = userDrugId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
