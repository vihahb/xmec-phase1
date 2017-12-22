package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Notification;

/**
 * Created by xtel on 11/8/17.
 */

public abstract class NotificationModel extends AbsICmd {
    //    http://x-mec.com/api/user/notification
    private BasicModel basicModel = new BasicModel();
    int type;


    protected NotificationModel(int type) {
        if (type != -1) {
            this.type = type;
        }
        run();
    }

    @Override
    protected void invoke() {
        if (type > 0){
            getNotificationUser();
        } else {
            getNotificationSystem();
        }
    }

    private void getNotificationUser() {
        String url = basicModel.BASE_API + basicModel.USER_NOTIFICAION;
        MyApplication.log("Notification model", url);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);
        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Notification>(RESP_Notification.class) {
            @Override
            protected void onSuccess(RESP_Notification obj) {
                NotificationModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                NotificationModel.this.onError(error.getCode());
            }
        });
    }

    private void getNotificationSystem() {
        String url = basicModel.BASE_API + basicModel.SYSTEM_NOTIFICAION;
        MyApplication.log("Notification model", url);
        basicModel.requestServer.getApi(url, null, new ResponseHandle<RESP_Notification>(RESP_Notification.class) {
            @Override
            protected void onSuccess(RESP_Notification obj) {
                NotificationModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                NotificationModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Notification obj);

    protected abstract void onError(int error);
}
