package com.xtelsolution.xmec.model.server;

import android.util.Log;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.resp.RESP_ImageUrl;

/**
 * Created by vivu on 12/14/17
 * xtel.vn
 */

public abstract class GetImageUrl extends AbsICmd {
    private static final String TAG = "GetImageUrl";
    private BasicModel basicModel= new BasicModel();

    public GetImageUrl() {
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.SYSTEM
                + basicModel.IMAGE_URL;

        Log.e(TAG, "url: " + url);

        basicModel.requestOkHttp.getApi(url, null, new ResponseHandle<RESP_ImageUrl>(RESP_ImageUrl.class) {
            @Override
            protected void onSuccess(RESP_ImageUrl obj) {
                GetImageUrl.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
    }

    protected abstract void onSuccess(RESP_ImageUrl url);

    protected abstract void onErrror(int code);
}
