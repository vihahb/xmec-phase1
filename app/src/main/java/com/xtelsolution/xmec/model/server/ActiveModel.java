package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class ActiveModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected ActiveModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_NIP + basicModel.ACTIVE_ACCOUNT;

        MyApplication.log("ActiveModel", url);
        MyApplication.log("ActiveModel", json);

        basicModel.requestServer.postApi(url, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                ActiveModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                ActiveModel.this.onError(error);
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(new Error(-1, "ERROR_REQUEST_RESPONSE", "ERROR"));
    }

    protected abstract void onSuccess(RESP_Basic obj);

    protected abstract void onError(Error error);
}