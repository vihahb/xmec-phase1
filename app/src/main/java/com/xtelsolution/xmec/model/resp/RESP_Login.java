package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/15/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Login extends RESP_Basic {
    @Expose
    private String authenticationid;
    @Expose
    private String session;
    @Expose
    private Long login_time;
    @Expose
    private Long expired_time;
    @Expose
    private Integer time_alive;
    @Expose
    private int dev_info_status;

    public RESP_Login() {
    }

    public String getAuthenticationid() {
        return authenticationid;
    }

    public void setAuthenticationid(String authenticationid) {
        this.authenticationid = authenticationid;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Long getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Long login_time) {
        this.login_time = login_time;
    }

    public Long getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Long expired_time) {
        this.expired_time = expired_time;
    }

    public Integer getTime_alive() {
        return time_alive;
    }

    public void setTime_alive(Integer time_alive) {
        this.time_alive = time_alive;
    }

    public int getDev_info_status() {
        return dev_info_status;
    }

    public void setDev_info_status(int dev_info_status) {
        this.dev_info_status = dev_info_status;
    }
}