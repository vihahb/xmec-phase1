package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/16/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Reset extends RESP_Basic {
    @Expose
    private String password;

    public RESP_Reset() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}