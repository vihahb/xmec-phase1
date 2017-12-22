package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.UserDrugs;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/30/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_UserDrugs extends RESP_Basic {
    @Expose
    private List<UserDrugs> data;

    public RESP_UserDrugs() {
    }

    public List<UserDrugs> getData() {
        return data;
    }

    public void setData(List<UserDrugs> data) {
        this.data = data;
    }
}