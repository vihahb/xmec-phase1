package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.DeviceInfo;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/19/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Authenticate {
    @Expose
    private String authenticationid;
    @Expose
    private String service_code;
    @Expose
    private DeviceInfo devInfo;

    public REQ_Authenticate() {
        devInfo = new DeviceInfo();
    }

    public String getAuthenticationid() {
        return authenticationid;
    }

    public void setAuthenticationid(String authenticationid) {
        this.authenticationid = authenticationid;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public DeviceInfo getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(DeviceInfo devInfo) {
        this.devInfo = devInfo;
    }
}