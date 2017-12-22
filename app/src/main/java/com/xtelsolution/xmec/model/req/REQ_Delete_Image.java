package com.xtelsolution.xmec.model.req;

import com.google.gson.annotations.Expose;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/1/2017
 * Email: leconglongvu@gmail.com
 */
public class REQ_Delete_Image {
    @Expose
    private String path;

    public REQ_Delete_Image() {
    }

    public REQ_Delete_Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
