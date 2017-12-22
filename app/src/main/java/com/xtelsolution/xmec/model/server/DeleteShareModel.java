package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Id;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class DeleteShareModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected DeleteShareModel(String json) {
        this.json = json;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.MBR + basicModel.MBR_SHARE;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("DeleteShareModel", "url " + url);
        MyApplication.log("DeleteShareModel", "session " + session);
        MyApplication.log("DeleteShareModel", "session " + json);

        basicModel.requestServer.deleteApi(url, session, json, new ResponseHandle<RESP_Id>(RESP_Id.class) {
            @Override
            protected void onSuccess(RESP_Id obj) {
                DeleteShareModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                DeleteShareModel.this.onError(error.getCode());
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