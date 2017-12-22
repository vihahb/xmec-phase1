package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.resp.RESP_ShareInfo;
import com.xtelsolution.xmec.model.resp.RESP_Shared;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 12/6/17
 * xtel.vn
 */

public abstract class GetShareInfoById extends AbsICmd {
    private BasicModel basicModel = new BasicModel();
    private int share_id;

    public GetShareInfoById(int share_id) {
        this.share_id = share_id;
        run();
    }

    @Override
    protected void invoke() {
    //http://x-mec.com/api/medical-report-book/share/info?id=951
        String url = basicModel.BASE_API +
                basicModel.MBR +
                basicModel.SHARE +
                basicModel.INFO + "?id=" + share_id;

        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("GetShareInfoById", "url: " + url);
        MyApplication.log("GetShareInfoById", "session: " + session);
        basicModel.requestOkHttp.getApi(url, session, new ResponseHandle<RESP_ShareInfo>(RESP_ShareInfo.class) {
            @Override
            protected void onSuccess(RESP_ShareInfo obj) {
                GetShareInfoById.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                GetShareInfoById.this.onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onErrror(ERROR_UNKOW);
    }

    protected abstract void onSuccess(RESP_ShareInfo shareInfo);

    protected abstract void onErrror(int code);
}
