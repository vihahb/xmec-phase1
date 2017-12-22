package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/16/2017
 * Email: leconglongvu@gmail.com
 */
public class UserInfo {
    @Expose
    private String first_name;
    @Expose
    private String last_name;

    public UserInfo(String name) {
        first_name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}