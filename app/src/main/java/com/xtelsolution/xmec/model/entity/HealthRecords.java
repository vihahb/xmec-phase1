package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.req.REQ_Hr_Create;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class HealthRecords extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
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
    @Expose
    private RealmList<UserDrugs> userDrugs;
    @Expose
    private RealmList<HealthRecordImages> healthRecordImages;
    @Expose
    private RealmList<UserDiseases> userDiseases;
    @Expose
    private RealmList<ScheduleDrug> scheduleDrugs;

    public HealthRecords() {
        userDrugs = new RealmList<>();
        healthRecordImages = new RealmList<>();
        userDiseases = new RealmList<>();
        scheduleDrugs = new RealmList<>();
    }

    public HealthRecords(REQ_Hr_Create hrCreate) {
        id = hrCreate.getId();
        name = hrCreate.getName();
//        dateCreate = hrCreate.getDateCreate();
        dateCreateLong = hrCreate.getDateCreateLong();
        endDateLong = hrCreate.getEndDateLong();
        note = hrCreate.getNote();
        mrbId = hrCreate.getMrbId();
        hospitalId = hrCreate.getHospitalId();
        hospitalName = hrCreate.getHospitalName();
        doctorId = hrCreate.getDoctorId();
        doctorName = hrCreate.getDoctorName();
        userDrugs = hrCreate.getUserDrugs();
        healthRecordImages = hrCreate.getHealthRecordImages();
        userDiseases = hrCreate.getUserDiseases();

        userDrugs = new RealmList<>();
        healthRecordImages = new RealmList<>();
        userDiseases = new RealmList<>();
        scheduleDrugs = new RealmList<>();
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

    public RealmList<ScheduleDrug> getScheduleDrugs() {
        return scheduleDrugs;
    }

    public void setScheduleDrugs(RealmList<ScheduleDrug> scheduleDrugs) {
        this.scheduleDrugs = scheduleDrugs;
    }
}