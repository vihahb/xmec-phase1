package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.ShareMbrEntity;

import java.util.List;

/**
 * Created by xtel on 11/14/17.
 */

public class RESP_ListShareLinkMbr extends RESP_Basic {

    @Expose
    private List<ShareMbrEntity> data;

    public List<ShareMbrEntity> getData() {
        return data;
    }

    public void setData(List<ShareMbrEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_ListShareLinkMbr{" +
                "data=" + data +
                '}';
    }
}
