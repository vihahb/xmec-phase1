package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.RatingObject;

import java.util.List;

/**
 * Created by vivu on 11/30/17
 * xtel.vn
 */

public class RESP_ListRate extends RESP_Basic {

    @Expose
    private List<RatingObject> data;

    public List<RatingObject> getData() {
        return data;
    }

    public void setData(List<RatingObject> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_ListRate{" +
                "data=" + data +
                '}';
    }
}
