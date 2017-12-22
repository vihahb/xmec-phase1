package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.CategoryListEntity;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public class RESP_CategoryListInfo extends RESP_Basic {

    @Expose
    private List<CategoryListEntity> data;

    public List<CategoryListEntity> getData() {
        return data;
    }

    public void setData(List<CategoryListEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_CategoryListInfo{" +
                "data=" + data +
                '}';
    }
}
