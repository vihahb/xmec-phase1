package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;

import java.util.List;

/**
 * Created by vivu on 11/27/17
 * xtel.vn
 */

public class DrugRecent {
    @Expose
    private List<DrugSearchEntity> data;

    public DrugRecent(List<DrugSearchEntity> data) {
        this.data = data;
    }

    public List<DrugSearchEntity> getData() {
        return data;
    }

    public void setData(List<DrugSearchEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DrugRecent{" +
                "data=" + data +
                '}';
    }
}
