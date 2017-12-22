package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.CategoryEntity;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

import java.util.List;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */

public class RESP_CategoryList extends RESP_Basic{

    @Expose
    private List<CategoryEntity> data;

    public List<CategoryEntity> getData() {
        return data;
    }

    public void setData(List<CategoryEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RESP_CategoryList{" +
                "data=" + data +
                '}';
    }
}
