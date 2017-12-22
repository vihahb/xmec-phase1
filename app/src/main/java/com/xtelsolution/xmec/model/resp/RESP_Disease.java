package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Created by vivu on 11/22/17.
 */

public class RESP_Disease extends RESP_Basic {

    @Expose
    private List<DiseaseObject> data;

    public RESP_Disease(List<DiseaseObject> data) {
        this.data = data;
    }

    public List<DiseaseObject> getData() {
        return data;
    }

    public void setData(List<DiseaseObject> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_Disease{" +
                "data=" + data +
                '}';
    }
}
