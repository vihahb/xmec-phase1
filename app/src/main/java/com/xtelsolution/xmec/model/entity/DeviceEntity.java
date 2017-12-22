package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by xtel on 11/10/17.
 */

public class DeviceEntity {
    @Expose
    private int uid;
    @Expose
    private String deviceId;
    @Expose
    private int status;
    @Expose
    private String fcmCloudkey;

    public DeviceEntity() {
    }

    public DeviceEntity(int uid, String deviceId, int status, String fcmCloudkey) {
        this.uid = uid;
        this.deviceId = deviceId;
        this.status = status;
        this.fcmCloudkey = fcmCloudkey;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFcmCloudkey() {
        return fcmCloudkey;
    }

    public void setFcmCloudkey(String fcmCloudkey) {
        this.fcmCloudkey = fcmCloudkey;
    }

    @Override
    public String toString() {
        return "DeviceEntity{" +
                "uid=" + uid +
                ", deviceId='" + deviceId + '\'' +
                ", status=" + status +
                ", fcmCloudkey='" + fcmCloudkey + '\'' +
                '}';
    }
}
