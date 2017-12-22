package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Created by vivu on 12/14/17
 * xtel.vn
 */

public class RESP_ImageUrl extends RESP_Basic {

    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RESP_ImageUrl{" +
                "url='" + url + '\'' +
                '}';
    }
}
