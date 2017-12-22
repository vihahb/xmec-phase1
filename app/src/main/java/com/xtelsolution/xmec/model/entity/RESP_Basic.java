package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Lê Công Long Vũ on 12/28/2016
 */

public class RESP_Basic implements Serializable {
    @Expose
    private Error error;

    public RESP_Basic() {
    }

    public RESP_Basic(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "RESP_Basic{" +
                "error=" + error +
                '}';
    }
}
