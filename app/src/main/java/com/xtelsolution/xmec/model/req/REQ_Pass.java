
package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/28/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Pass {

    @Expose
    private int id;
    @Expose
    private String oldPassword;
    @Expose
    private String password;
    @Expose
    private int type;
    @Expose
    private String username;

    public REQ_Pass() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}