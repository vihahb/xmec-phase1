package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.entity.UserDrugs;

import java.io.Serializable;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Hr_Create implements Serializable {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private long dateCreateLong;
    @Expose
    private long endDateLong;
    @Expose
    private String note;
    @Expose
    private int mrbId;
    @Expose
    private int hospitalId;
    @Expose
    private String hospitalName;
    @Expose
    private int doctorId;
    @Expose
    private String doctorName;

    private RealmList<UserDrugs> userDrugs;
    private RealmList<HealthRecordImages> healthRecordImages;
    private RealmList<UserDiseases> userDiseases;

    public REQ_Hr_Create() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateCreateLong() {
        return dateCreateLong;
    }

    public void setDateCreateLong(long dateCreateLong) {
        this.dateCreateLong = dateCreateLong;
    }

    public long getEndDateLong() {
        return endDateLong;
    }

    public void setEndDateLong(long endDateLong) {
        this.endDateLong = endDateLong;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMrbId() {
        return mrbId;
    }

    public void setMrbId(int mrbId) {
        this.mrbId = mrbId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public RealmList<UserDrugs> getUserDrugs() {
        return userDrugs;
    }

    public void setUserDrugs(RealmList<UserDrugs> userDrugs) {
        this.userDrugs = userDrugs;
    }

    public RealmList<HealthRecordImages> getHealthRecordImages() {
        return healthRecordImages;
    }

    public void setHealthRecordImages(RealmList<HealthRecordImages> healthRecordImages) {
        this.healthRecordImages = healthRecordImages;
    }

    public RealmList<UserDiseases> getUserDiseases() {
        return userDiseases;
    }

    public void setUserDiseases(RealmList<UserDiseases> userDiseases) {
        this.userDiseases = userDiseases;
    }
}