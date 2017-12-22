package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by xtel on 11/8/17.
 */

public class NotificationAction implements Serializable{

    @Expose
    public int mrb_id;
    @Expose
    public String mrb_name;
    @Expose
    public int action_type;
    @Expose
    public int state;
    @Expose
    public int share_id;

    public NotificationAction() {
    }

    public NotificationAction(int mrb_id, String mrb_name, int action_type, int state, int share_id) {
        this.mrb_id = mrb_id;
        this.mrb_name = mrb_name;
        this.action_type = action_type;
        this.state = state;
        this.share_id = share_id;
    }

    public int getMrb_id() {
        return mrb_id;
    }

    public void setMrb_id(int mrb_id) {
        this.mrb_id = mrb_id;
    }

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getShare_id() {
        return share_id;
    }

    public void setShare_id(int share_id) {
        this.share_id = share_id;
    }

    public String getMrb_name() {
        return mrb_name;
    }

    public void setMrb_name(String mrb_name) {
        this.mrb_name = mrb_name;
    }

    @Override
    public String toString() {
        return "NotificationAction{" +
                "mrb_id=" + mrb_id +
                ", action_type=" + action_type +
                ", state=" + state +
                ", share_id=" + share_id +
                '}';
    }
}
