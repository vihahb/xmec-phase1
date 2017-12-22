package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by xtel on 11/15/17.
 */

public class DeleteShare {
    @Expose
    private int id;
    private int type;

    public DeleteShare() {
    }

    public DeleteShare(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DeleteShare{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
