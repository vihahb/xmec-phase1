package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.resp.RESP_Schedule_Drug;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class AddListScheduleModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected AddListScheduleModel(String json) {
        this.json = json;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.SCHEDULE).concat(basicModel.LIST);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("AddListScheduleModel", "url " + url);
        MyApplication.log("AddListScheduleModel", "session " + session);
        MyApplication.log("AddListScheduleModel", "session " + json);

        basicModel.requestServer.postApi(url, session, json, new ResponseHandle<RESP_Schedule_Drug>(RESP_Schedule_Drug.class) {
            @Override
            protected void onSuccess(RESP_Schedule_Drug obj) {
                AddListScheduleModel.this.onSuccess(obj.getData());
            }

            @Override
            protected void onError(Error error) {
                AddListScheduleModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(List<ScheduleDrug> scheduleDrugs);

    protected abstract void onError(int errorCode);
}