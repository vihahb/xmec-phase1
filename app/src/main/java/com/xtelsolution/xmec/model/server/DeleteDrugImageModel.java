package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class DeleteDrugImageModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected DeleteDrugImageModel(String json) {
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.HR).concat(basicModel.DRUG).concat(basicModel.IMAGE);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("DeleteDrugImageModel", "url " + url);
        MyApplication.log("DeleteDrugImageModel", "session " + session);
        MyApplication.log("DeleteDrugImageModel", "session " + json);

        basicModel.requestServer.deleteApi(url, session, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                DeleteDrugImageModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                DeleteDrugImageModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(Constants.ERROR_UNKOW);
    }

    protected abstract void onSuccess();

    protected abstract void onError(int errorCode);
}