package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.SerializedName;
import com.xtelsolution.xmec.model.entity.NewCategory;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Created by ThanhChung on 12/6/17.
 */

public class RESP_NewCategory extends RESP_Basic {

    @SerializedName("data")
    private List<NewCategory> data;

    @Override
    public String toString() {
        return "RESP_NewCategory{" +
                "data=" + data +
                '}';
    }

    public List<NewCategory> getData() {
        return data;
    }

    public void setData(List<NewCategory> data) {
        this.data = data;
    }

}
