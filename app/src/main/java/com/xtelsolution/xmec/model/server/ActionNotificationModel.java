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
 * Created by xtel on 11/9/17.
 */

public abstract class ActionNotificationModel extends AbsICmd {

    BasicModel basicModel = new BasicModel();

    String json;

    public ActionNotificationModel(String json) {
        this.json = json;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.USER_NOTIFICAION;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("ActionNotificationModel", "url " + url);
        MyApplication.log("ActionNotificationModel", "session " + session);
        MyApplication.log("ActionNotificationModel", "json " + json);

        basicModel.requestServer.putApi(url, session, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                ActionNotificationModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                ActionNotificationModel.this.onError(error.getCode());
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
