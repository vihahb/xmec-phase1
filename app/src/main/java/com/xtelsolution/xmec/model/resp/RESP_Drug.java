package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/27/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Drug extends RESP_Basic {
    @Expose
    private List<Drug> data;

    public RESP_Drug() {
    }

    public RESP_Drug(List<Drug> data) {
        this.data = data;
    }

    public List<Drug> getData() {
        return data;
    }

    public void setData(List<Drug> data) {
        this.data = data;
    }
}