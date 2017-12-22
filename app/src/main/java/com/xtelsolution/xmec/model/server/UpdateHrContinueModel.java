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
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class UpdateHrContinueModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected UpdateHrContinueModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.HR).concat(basicModel.CONTINUE);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("UpdateHrContinueModel", "url " + url);
        MyApplication.log("UpdateHrContinueModel", "session " + session);
        MyApplication.log("UpdateHrContinueModel", "json " + json);

        basicModel.requestServer.putApi(url, session, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                UpdateHrContinueModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                UpdateHrContinueModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess();

    protected abstract void onError(int errorCode);
}