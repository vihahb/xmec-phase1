package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class RESP_Schedule_Drug extends RESP_Basic {
    @Expose
    private List<ScheduleDrug> data;

    public RESP_Schedule_Drug() {
    }

    public RESP_Schedule_Drug(ScheduleDrug scheduleDrug) {
        data = new ArrayList<>();
        data.add(scheduleDrug);
    }

    public List<ScheduleDrug> getData() {
        return data;
    }

    public void setData(List<ScheduleDrug> data) {
        this.data = data;
    }
}