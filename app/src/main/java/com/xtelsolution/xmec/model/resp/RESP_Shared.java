package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.Shared;

import io.realm.RealmList;

/**
 * Created by lecon on 12/4/2017
 */

public class RESP_Shared extends RESP_Basic {
    @Expose
    private RealmList<Shared> data;

    public RESP_Shared() {
    }

    public RealmList<Shared> getData() {
        return data;
    }

    public void setData(RealmList<Shared> data) {
        this.data = data;
    }
}