package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.sdk.commons.Constants;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Active {
    @Expose
    private String authorization_code;
    @Expose
    private String activation_code;
    @Expose
    private String accountType;
    @Expose
    private String service_code;

    public REQ_Active() {
        accountType = Constants.PHONE_TYPE;
        service_code = Constants.SERVICE_CODE;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }
}