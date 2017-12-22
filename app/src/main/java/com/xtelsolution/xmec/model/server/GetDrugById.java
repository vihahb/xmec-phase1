package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_DrugInfo;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public abstract class GetDrugById extends AbsICmd {

    private BasicModel basicModel = new BasicModel();
    private int id;

    public GetDrugById(int id) {
        this.id = id;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API +
                basicModel.SYSTEM +
                basicModel.DRUG +
                basicModel.DRUG_DETAIL + id;

        MyApplication.log("GetDrugById url", url);
        basicModel.requestOkHttp.getApi(url, null, new ResponseHandle<RESP_DrugInfo>(RESP_DrugInfo.class) {
            @Override
            protected void onSuccess(RESP_DrugInfo obj) {
                GetDrugById.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_DrugInfo drugInfo);

    protected abstract void onErrror(int code);
}
