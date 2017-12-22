package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by xtel on 11/13/17.
 */

public class SharedStateEntity {
    @Expose
    private int id;
    @Expose
    private int shareState;

    public SharedStateEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShareState() {
        return shareState;
    }

    public void setShareState(int shareState) {
        this.shareState = shareState;
    }

    @Override
    public String toString() {
        return "SharedStateEntity{" +
                "id=" + id +
                ", shareState=" + shareState +
                '}';
    }
}
