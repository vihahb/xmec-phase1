package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Reset;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class ResetModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected ResetModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_NIP + basicModel.RESET_PASSWORD;

        MyApplication.log("ResetModel", url);
        MyApplication.log("ResetModel", json);

        basicModel.requestServer.putApi(url, json, new ResponseHandle<RESP_Reset>(RESP_Reset.class) {
            @Override
            protected void onSuccess(RESP_Reset obj) {
                ResetModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                ResetModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Reset obj);

    protected abstract void onError(int errorCode);
}