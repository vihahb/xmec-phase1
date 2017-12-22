package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.entity.DeviceInfo;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/15/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Login {
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String service_code;
    @Expose
    private String accountType;
    @Expose
    private DeviceInfo devInfo;

    public REQ_Login() {
        service_code = Constants.SERVICE_CODE;
        accountType = Constants.PHONE_TYPE;
        devInfo = new DeviceInfo();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public DeviceInfo getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(DeviceInfo devInfo) {
        this.devInfo = devInfo;
    }
}