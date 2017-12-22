package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class ScheduleTimePerDay extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
    private int id;
    /**
     * Theo lần: Số lần/ngày
     * Theo giờ: Số giờ/lần
     */
    @Expose
    private int time;
    /**
     * 1: Theo lần
     * 2: Theo giờ
     */
    @Expose
    private int scheduleType;
    /**
     * Giờ bắt đầu
     */
    @Expose
    private String startTime;
    /**
     * Định nghĩa khi lưu theo giờ
     */
    @Expose
    private String endTime;
    @Expose
    private int scheduleDrugId;

    public ScheduleTimePerDay() {
        id = -1;
    }

    public ScheduleTimePerDay(int time, int scheduleType) {
        id = -1;
        this.time = time;
        this.scheduleType = scheduleType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getScheduleDrugId() {
        return scheduleDrugId;
    }

    public void setScheduleDrugId(int scheduleDrugId) {
        this.scheduleDrugId = scheduleDrugId;
    }
}