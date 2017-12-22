package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_DiseaseObject;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 11/23/17.
 */

public abstract class GetDiseaseByIdModel extends AbsICmd {

    private BasicModel basicModel = new BasicModel();
    int id;

    public GetDiseaseByIdModel(int id) {
        this.id = id;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.SYSTEM
                + basicModel.DISEASE
                + basicModel.DISEASE_DETAIL + id;
        MyApplication.log("GetDiseaseByIdModel", "url " + url);

        basicModel.requestServer.getApi(url, null, new ResponseHandle<RESP_DiseaseObject>(RESP_DiseaseObject.class) {
            @Override
            protected void onSuccess(RESP_DiseaseObject obj) {
                GetDiseaseByIdModel.this.onSuccess(obj.getDiseaseObject());
            }

            @Override
            protected void onError(Error error) {
                GetDiseaseByIdModel.this.onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onErrror(ERROR_UNKOW);
    }

    protected abstract void onSuccess(DiseaseObject object);

    protected abstract void onErrror(int code);
}
