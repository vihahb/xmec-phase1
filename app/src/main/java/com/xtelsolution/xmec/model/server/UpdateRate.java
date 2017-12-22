package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.RateObject;

/**
 * Created by vivu on 11/30/17
 * xtel.vn
 */

public abstract class UpdateRate extends AbsICmd {

    private static final String TAG = "UpdateRate";
    RateObject object;
    private BasicModel basicModel = new BasicModel();

    public UpdateRate(RateObject object) {
        this.object = object;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API +
                basicModel.HOSPITAL_RATE;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);
        String jsonObject = JsonHelper.toJson(object);

        MyApplication.log(TAG, "ủrl: " + url);
        MyApplication.log(TAG, "session: " + session);
        MyApplication.log(TAG, "jsonObject: " + jsonObject);

        basicModel.requestOkHttp.putApi(url, jsonObject, session, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                UpdateRate.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                UpdateRate.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Basic respId);

    protected abstract void onError(int errorCode);
}
