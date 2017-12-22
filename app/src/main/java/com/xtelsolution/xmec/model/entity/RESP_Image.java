package com.xtelsolution.xmec.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Image implements Serializable {
    private List<Object> data;

    public RESP_Image(List<Object> data) {
        this.data = data;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}