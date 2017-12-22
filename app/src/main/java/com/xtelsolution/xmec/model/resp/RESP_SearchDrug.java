package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public class RESP_SearchDrug extends RESP_Basic {
    @Expose
    private List<DrugSearchEntity> data;

    public List<DrugSearchEntity> getData() {
        return data;
    }

    public void setData(List<DrugSearchEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_SearchDrug{" +
                "data=" + data +
                '}';
    }
}
