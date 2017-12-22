package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Login;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class LoginModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected LoginModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_NIP + basicModel.LOGIN;

        MyApplication.log("LoginModel", url);

        basicModel.requestServer.postApi(url, json, new ResponseHandle<RESP_Login>(RESP_Login.class) {
            @Override
            protected void onSuccess(RESP_Login obj) {
                LoginModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                LoginModel.this.onError(error);
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(new Error(-1, "ERROR_REQUEST_RESPONSE", "ERROR"));
    }

    protected abstract void onSuccess(RESP_Login obj);

    protected abstract void onError(Error error);
}