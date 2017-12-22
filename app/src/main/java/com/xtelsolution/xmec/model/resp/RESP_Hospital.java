package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/24/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Hospital extends RESP_Basic {
    @Expose
    private List<Hospital> data;

    public RESP_Hospital() {
    }

    public List<Hospital> getData() {
        return data;
    }

    public void setData(List<Hospital> data) {
        this.data = data;
    }
}