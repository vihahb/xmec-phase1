package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/15/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_ReActive extends RESP_Basic {
    @Expose
    private String username;
    @Expose
    private String service_code;
    @Expose
    private int sendMail;
    @Expose
    private String accountType;

    public REQ_ReActive() {
        service_code = Constants.SERVICE_CODE;
        sendMail = 0;
        accountType = Constants.PHONE_TYPE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public int getSendMail() {
        return sendMail;
    }

    public void setSendMail(int sendMail) {
        this.sendMail = sendMail;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}