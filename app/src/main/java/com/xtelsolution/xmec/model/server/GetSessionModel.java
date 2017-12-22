package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.req.REQ_Authenticate;
import com.xtelsolution.xmec.model.resp.RESP_Login;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class GetSessionModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected GetSessionModel() {
        String auth = SharedUtils.getInstance().getStringValue(Constants.AUTHENTICATION_ID);

        if (auth != null) {
            REQ_Authenticate authenticate = new REQ_Authenticate();

            authenticate.setAuthenticationid(auth);
            authenticate.setService_code(Constants.SERVICE_CODE);
            json = JsonHelper.toJson(authenticate);

            run();
        } else {
            onErrror();
        }
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_NIP + basicModel.AUTHENTICATE;

        MyApplication.log("GetSessionModel", "url " + url);
        MyApplication.log("GetSessionModel", "json " + json);

        basicModel.requestServer.postApi(url, json, new ResponseHandle<RESP_Login>(RESP_Login.class) {
            @Override
            protected void onSuccess(RESP_Login obj) {

                SharedUtils.getInstance().putStringValue(Constants.SESSION, obj.getSession());
                SharedUtils.getInstance().putLongValue(Constants.LOGIN_TIME, obj.getLogin_time());
                SharedUtils.getInstance().putLongValue(Constants.EXPIRED_TIME, obj.getExpired_time());
                SharedUtils.getInstance().putIntValue(Constants.TIME_ALIVE, obj.getTime_alive());
                SharedUtils.getInstance().putIntValue(Constants.DEV_INFO_STATUS, obj.getDev_info_status());

                GetSessionModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                onErrror();
            }
        });
    }

    @Override
    protected void exception(String message) {
        onErrror();
    }

    protected abstract void onSuccess();

    protected abstract void onErrror();
}