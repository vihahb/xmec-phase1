package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/26/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Hr_Image {
    @Expose
    private String urlImage;
    @Expose
    private int heathRecordId;

    public REQ_Hr_Image() {
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getHeathRecordId() {
        return heathRecordId;
    }

    public void setHeathRecordId(int heathRecordId) {
        this.heathRecordId = heathRecordId;
    }
}