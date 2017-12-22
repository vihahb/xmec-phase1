package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/16/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Update_Schedule extends RESP_Basic {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private long dateCreateLong;
    /**
     * số ngày uống
     * 0 - Không giới hạn
     */
    @Expose
    private int dayDuration;
    /**
     * kiểu chu kỳ:
     * 1: Hằng ngày
     * 2: Kiểu cách ngày
     * 3: Kiểu Quartz
     */
    @Expose
    private int periodType;
    /**
     * 1. Kiểu hằng ngày: null
     * 2. Kiểu cách ngày [số]. Vd: 2
     * 3. Kiểu của Quartz:
     * Các ngày trong tuần [1-7], cn là 1. Vd: 2,4,6
     */
    @Expose
    private String period;
    /**
     * 1: Uống sau khi ăn
     * 2: Uống trước khi ăn
     * 3: Uống sau khi ăn 1 h
     */
    @Expose
    private int notice;
    /**
     * Active: 1 - Deactive: 2
     */
    @Expose
    private int state;
    /**
     * Id sổ khám
     */
    @Expose
    private int healthRecordId;
    @Expose
//    @Ignore
    private RealmList<UserDrugSchedule> userDrugSchedules;
    @Expose
//    @Ignore
    private RealmList<ScheduleTimePerDay> scheduleTimePerDays;

    public RESP_Update_Schedule() {
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

    public int getDayDuration() {
        return dayDuration;
    }

    public void setDayDuration(int dayDuration) {
        this.dayDuration = dayDuration;
    }

    public int getPeriodType() {
        return periodType;
    }

    public void setPeriodType(int periodType) {
        this.periodType = periodType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(int healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public RealmList<UserDrugSchedule> getUserDrugSchedules() {
        return userDrugSchedules;
    }

    public void setUserDrugSchedules(RealmList<UserDrugSchedule> userDrugSchedules) {
        this.userDrugSchedules = userDrugSchedules;
    }

    public RealmList<ScheduleTimePerDay> getScheduleTimePerDays() {
        return scheduleTimePerDays;
    }

    public void setScheduleTimePerDays(RealmList<ScheduleTimePerDay> scheduleTimePerDays) {
        this.scheduleTimePerDays = scheduleTimePerDays;
    }
}