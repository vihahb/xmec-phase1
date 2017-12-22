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
public abstract class DeleteSharedModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected DeleteSharedModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.MBR).concat(basicModel.SHARE);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("DeleteSharedModel", "url " + url);
        MyApplication.log("DeleteSharedModel", "session " + session);
        MyApplication.log("DeleteSharedModel", "json " + json);

        basicModel.requestServer.deleteApi(url, session, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                DeleteSharedModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                DeleteSharedModel.this.onError(error.getCode());
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