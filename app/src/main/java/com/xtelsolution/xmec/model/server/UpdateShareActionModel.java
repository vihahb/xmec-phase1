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
 * Created by xtel on 11/15/17.
 */

public abstract class UpdateShareActionModel extends AbsICmd {

    private BasicModel basicModel = new BasicModel();

    private String json;

    public UpdateShareActionModel(String json) {
        this.json = json;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.MBR + basicModel.MBR_SHARE + basicModel.SHARE_TYPE;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("UpdateShareActionModel", "url " + url);
        MyApplication.log("UpdateShareActionModel", "session " + session);
        MyApplication.log("UpdateShareActionModel", "json " + json);

        basicModel.requestServer.putApi(url, session, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                UpdateShareActionModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                UpdateShareActionModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Basic resp_basic);

    protected abstract void onError(int errorCode);
}
