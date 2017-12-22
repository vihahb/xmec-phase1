package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Register;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class ReActiveModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected ReActiveModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_NIP + basicModel.RE_ACTIVE_ACCOUNT;

        MyApplication.log("ReActiveModel", url);

        basicModel.requestServer.putApi(url, json, new ResponseHandle<RESP_Register>(RESP_Register.class) {
            @Override
            protected void onSuccess(RESP_Register obj) {
                ReActiveModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                ReActiveModel.this.onError(error);
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(new Error(-1, "ERROR_REQUEST_RESPONSE", "ERROR"));
    }

    protected abstract void onSuccess(RESP_Register obj);

    protected abstract void onError(Error error);
}