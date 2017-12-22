package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Upload_Image extends RESP_Basic {
    @Expose
    private String full_path_server;
    @Expose
    private String path;

    public RESP_Upload_Image() {
    }

    public String getFull_path_server() {
        return full_path_server;
    }

    public void setFull_path_server(String full_path_server) {
        this.full_path_server = full_path_server;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}