package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class UserDiseases extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
    private int id;
    @Expose
    private String name;
    @Expose
    private int diseaseId;
    @Expose
    private int hrId;

    public UserDiseases() {
        id = -1;
        diseaseId = -1;
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

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public int getHrId() {
        return hrId;
    }

    public void setHrId(int hrId) {
        this.hrId = hrId;
    }
}