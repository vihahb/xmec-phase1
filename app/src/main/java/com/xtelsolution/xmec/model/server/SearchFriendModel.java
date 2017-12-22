package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Friend;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SearchFriendModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String phone;

    protected SearchFriendModel(String phone) {
        this.phone = phone;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.SEARCH_PHONE + phone;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("SearchFriendModel", "url " + url);
        MyApplication.log("SearchFriendModel", "session " + session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Friend>(RESP_Friend.class) {
            @Override
            protected void onSuccess(RESP_Friend obj) {
                SearchFriendModel.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_Friend respFriend);

    protected abstract void onErrror(int code);
}