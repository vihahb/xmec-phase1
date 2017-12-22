package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Friends extends RESP_Basic implements Serializable {
    @Expose
    private List<Friends> data;

    public RESP_Friends() {
    }

    public RESP_Friends(List<Friends> data) {
        this.data = data;
    }

    public List<Friends> getData() {
        return data;
    }

    public void setData(List<Friends> data) {
        this.data = data;
    }
}