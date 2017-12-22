package com.xtelsolution.xmec.model.server;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.DeviceEntity;
import com.xtelsolution.xmec.model.entity.DeviceInfo;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class UpdateFcmKeyModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    int status;

    protected UpdateFcmKeyModel(int status) {
        this.status = status;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.USER).concat(basicModel.DEVICE);

        DeviceInfo info = new DeviceInfo();
        String token = FirebaseInstanceId.getInstance().getToken();

//        token = token != null ? token : "";
        if (token == null){
            token = "";
        }
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);
        int uid = SharedUtils.getInstance().getIntValue(Constants.USER_UID);
        String deviceId = info.getDeviceid();

        DeviceEntity deviceEntity = new DeviceEntity(uid, deviceId, status, token);

        MyApplication.log("UpdateFcmKeyModel", "url " + url);
        MyApplication.log("UpdateFcmKeyModel", "session " + session);
        MyApplication.log("deviceEntity", "deviceEntity " + deviceEntity.toString());

        basicModel.requestServer.putApi(url, session, JsonHelper.toJson(deviceEntity), new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                UpdateFcmKeyModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                UpdateFcmKeyModel.this.onError(error.getCode());
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