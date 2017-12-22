package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Created by vivu on 12/6/17
 * xtel.vn
 */

public class RESP_ShareInfo extends RESP_Basic {

    @Expose
    public int id;
    @Expose
    public int uid;
    @Expose
    public String dateCreate;
    @Expose
    public int mrbId;
    @Expose
    public int shareType;
    @Expose
    public int shareTo;
    @Expose
    public int shareState;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public int getMrbId() {
        return mrbId;
    }

    public void setMrbId(int mrbId) {
        this.mrbId = mrbId;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public int getShareTo() {
        return shareTo;
    }

    public void setShareTo(int shareTo) {
        this.shareTo = shareTo;
    }

    public int getShareState() {
        return shareState;
    }

    public void setShareState(int shareState) {
        this.shareState = shareState;
    }

    @Override
    public String toString() {
        return "RESP_ShareInfo{" +
                "id=" + id +
                ", uid=" + uid +
                ", dateCreate='" + dateCreate + '\'' +
                ", mrbId=" + mrbId +
                ", shareType=" + shareType +
                ", shareTo=" + shareTo +
                ", shareState=" + shareState +
                '}';
    }
}
