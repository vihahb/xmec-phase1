package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.UserDrugs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/30/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_UserDrugs implements Serializable {
    @Expose
    private List<UserDrugs> data;

    public REQ_UserDrugs() {
        data = new ArrayList<>();
    }

    public REQ_UserDrugs(List<UserDrugs> data) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    public List<UserDrugs> getData() {
        return data;
    }

    public void setData(List<UserDrugs> data) {
        this.data = data;
    }
}