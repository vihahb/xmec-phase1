package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_UserInfo;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class GetUserInfoModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    protected GetUserInfoModel() {
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.USER_INFO);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("GetUserInfoModel", url);
        MyApplication.log("GetUserInfoModel", session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_UserInfo>(RESP_UserInfo.class) {
            @Override
            protected void onSuccess(RESP_UserInfo obj) {
                GetUserInfoModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onErrror(ERROR_UNKOW);
    }

    protected abstract void onSuccess(RESP_UserInfo respUserInfo);

    protected abstract void onErrror(int code);
}