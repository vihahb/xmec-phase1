package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by xtel on 11/8/17.
 */

public class NotificationData {
    @Expose
    public int uid;
    @Expose
    public String name;
    @Expose
    public int type;
    @Expose
    public String title;
    @Expose
    public String body;
    @Expose
    public NotificationAction action;

    public NotificationData() {
    }

    public NotificationData(int uid, String name, int type, String title, String body, NotificationAction action) {
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.title = title;
        this.body = body;
        this.action = action;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NotificationAction getAction() {
        return action;
    }

    public void setAction(NotificationAction action) {
        this.action = action;
    }
}
