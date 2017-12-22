package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/19/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Mrb extends RESP_Basic {
    @Expose
    private RealmList<Mbr> data;

    public RESP_Mrb() {
    }

    public RealmList<Mbr> getData() {
        return data;
    }

    public void setData(RealmList<Mbr> data) {
        this.data = data;
    }
}