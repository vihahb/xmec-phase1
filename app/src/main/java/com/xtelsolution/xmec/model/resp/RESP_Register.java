package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Register extends RESP_Basic {
    @Expose
    private String active_url;
    @Expose
    private String activation_code;

    public RESP_Register() {
    }

    public String getActive_url() {
        return active_url;
    }

    public void setActive_url(String active_url) {
        this.active_url = active_url;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }
}