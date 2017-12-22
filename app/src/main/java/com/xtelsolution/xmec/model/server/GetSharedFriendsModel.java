package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Friends;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class GetSharedFriendsModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    protected GetSharedFriendsModel() {
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.MBR + basicModel.MBR_SHARE +
                basicModel.MBR_USER + basicModel.TYPE + 2;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("GetSharedFriendsModel", "url " + url);
        MyApplication.log("GetSharedFriendsModel", "session " + session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Friends>(RESP_Friends.class) {
            @Override
            protected void onSuccess(RESP_Friends obj) {
                GetSharedFriendsModel.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_Friends respFriend);

    protected abstract void onErrror(int code);
}