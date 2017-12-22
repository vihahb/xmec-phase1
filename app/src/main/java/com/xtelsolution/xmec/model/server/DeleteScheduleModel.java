package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class DeleteScheduleModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private int id;

    protected DeleteScheduleModel(int id) {
        this.id = id;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.SCHEDULE).concat(basicModel.SD_ID).concat(String.valueOf(id));
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("DeleteScheduleModel", "session " + url);
        MyApplication.log("DeleteScheduleModel", "session " + session);

        basicModel.requestServer.deleteApi(url, session, "", new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                DeleteScheduleModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                DeleteScheduleModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(Constants.ERROR_UNKOW);
    }

    protected abstract void onSuccess();

    protected abstract void onError(int errorCode);
}