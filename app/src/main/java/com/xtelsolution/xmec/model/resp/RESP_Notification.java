package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.ObjectNotify;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Created by xtel on 11/8/17.
 */

public class RESP_Notification extends RESP_Basic{

    @Expose
    private List<ObjectNotify> data;

    public List<ObjectNotify> getData() {
        return data;
    }

    public void setData(List<ObjectNotify> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_Notification{" +
                "data=" + data +
                '}';
    }
}
