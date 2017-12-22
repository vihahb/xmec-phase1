package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/20/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Mbr_Update extends REQ_Mbr_Create {
    @Expose
    private int mrbId;

    public REQ_Mbr_Update() {
    }

    public int getMrbId() {
        return mrbId;
    }

    public void setMrbId(int mrbId) {
        this.mrbId = mrbId;
    }
}