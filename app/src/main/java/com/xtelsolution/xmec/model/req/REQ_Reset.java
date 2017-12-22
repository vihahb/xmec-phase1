package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.sdk.commons.Constants;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/16/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Reset {
    @Expose
    private String email;
    @Expose
    private String service_code;
    @Expose
    private Integer sendMail;
    @Expose
    private String accountType;
    @Expose
    private String password;
    @Expose
    private String authorization_code;

    public REQ_Reset() {
        service_code = Constants.SERVICE_CODE;
        sendMail = 0;
        accountType = Constants.PHONE_TYPE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public Integer getSendMail() {
        return sendMail;
    }

    public void setSendMail(Integer sendMail) {
        this.sendMail = sendMail;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }
}