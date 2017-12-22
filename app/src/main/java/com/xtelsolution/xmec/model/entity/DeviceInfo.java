package com.xtelsolution.xmec.model.entity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.Settings.Secure;

import com.google.gson.annotations.Expose;
import com.xtelsolution.MyApplication;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/14/2017
 * Email: leconglongvu@gmail.com
 */
public class DeviceInfo {
    @Expose
    private String deviceid;
    @Expose
    private String os_name;
    @Expose
    private String os_version;
    @Expose
    private String other;
    @Expose
    private int type;
    @Expose
    private String vendor;

    @SuppressLint("HardwareIds")
    public DeviceInfo() {
//        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        if (telephonyManager != null) {
//            if (ActivityCompat.checkSelfPermission(MyApplication.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            deviceid = telephonyManager.getDeviceId();
//        }

        deviceid = Secure.getString(MyApplication.context.getContentResolver(), Secure.ANDROID_ID);
        os_name = "Android";
        os_version = Build.VERSION.RELEASE;
        other = "none";
        type = 1;
        vendor = android.os.Build.MANUFACTURER;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getOs_name() {
        return os_name;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}