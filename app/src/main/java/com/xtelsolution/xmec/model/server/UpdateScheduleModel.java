package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Update_Schedule;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class UpdateScheduleModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected UpdateScheduleModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.SCHEDULE);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("UpdateScheduleModel", "url " + url);
        MyApplication.log("UpdateScheduleModel", "session " + session);
        MyApplication.log("UpdateScheduleModel", "session " + json);

        basicModel.requestServer.putApi(url, session, json, new ResponseHandle<RESP_Update_Schedule>(RESP_Update_Schedule.class) {
            @Override
            protected void onSuccess(RESP_Update_Schedule obj) {
                UpdateScheduleModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                UpdateScheduleModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Update_Schedule obj);

    protected abstract void onError(int errorCode);
}