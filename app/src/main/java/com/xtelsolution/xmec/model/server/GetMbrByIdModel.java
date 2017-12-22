package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.resp.RESP_Mrb;

import io.realm.RealmList;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by xtel on 11/9/17
 */

public abstract class GetMbrByIdModel extends AbsICmd {

    private BasicModel basicModel = new BasicModel();

    int mbrId;

    public GetMbrByIdModel(int mbrId) {
        this.mbrId = mbrId;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.MBR + basicModel.MBR_BY_ID + mbrId;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);
        MyApplication.log("GetMbrByIdModel", url);
        MyApplication.log("GetMbrByIdModel Sesion", session);
        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Mrb>(RESP_Mrb.class) {
            @Override
            protected void onSuccess(RESP_Mrb obj) {
                GetMbrByIdModel.this.onSuccess(obj.getData());
            }

            @Override
            protected void onError(Error error) {
                GetMbrByIdModel.this.onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onErrror(ERROR_UNKOW);
    }

    protected abstract void onSuccess(RealmList<Mbr> mbrList);

    protected abstract void onErrror(int code);
}
