package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Mrb;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class GetSharedAccountModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    protected GetSharedAccountModel() {
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.MBR).concat(basicModel.SHARED);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("GetSharedAccountModel", url);
        MyApplication.log("GetSharedAccountModel", session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Mrb>(RESP_Mrb.class) {
            @Override
            protected void onSuccess(RESP_Mrb obj) {
                GetSharedAccountModel.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_Mrb respMrb);

    protected abstract void onErrror(int code);
}