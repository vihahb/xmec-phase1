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
 * Created by vivu on 12/19/17
 * xtel.vn
 */

public abstract class DeleteNotification extends AbsICmd {
    private static final String TAG = "DeleteNotification";
    private BasicModel basicModel = new BasicModel();
    private int id;

    public DeleteNotification(int id_delete) {
        id = id_delete;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.USER_NOTIFICAION + "?id=" + id;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);
        MyApplication.log(TAG, "url: " + url);
        MyApplication.log(TAG, "Session: " + session);
        basicModel.requestOkHttp.deleteApi(url, "", session, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                DeleteNotification.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                DeleteNotification.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Basic obj);

    protected abstract void onError(int error);
}
