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
public abstract class DeleteDiseaseModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private int id;
    private String json;

    protected DeleteDiseaseModel(int id, String json) {
        this.id = id;
        this.json = json;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API.concat(basicModel.HR).concat(basicModel.DISEASE).concat(basicModel.UD_ID).concat(String.valueOf(id));
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("DeleteDiseaseModel", "url " + url);
        MyApplication.log("DeleteDiseaseModel", "session " + session);
        MyApplication.log("DeleteDiseaseModel", "session " + json);

        basicModel.requestServer.deleteApi(url, session, json, new ResponseHandle<RESP_Basic>(RESP_Basic.class) {
            @Override
            protected void onSuccess(RESP_Basic obj) {
                DeleteDiseaseModel.this.onSuccess();
            }

            @Override
            protected void onError(Error error) {
                DeleteDiseaseModel.this.onError(error.getCode());
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