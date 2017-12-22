package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.UserDiseases;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_User_Diseases extends RESP_Basic implements Serializable {
    @Expose
    private List<UserDiseases> data;

    public RESP_User_Diseases() {
    }

    public List<UserDiseases> getData() {
        return data;
    }

    public void setData(List<UserDiseases> data) {
        this.data = data;
    }
}