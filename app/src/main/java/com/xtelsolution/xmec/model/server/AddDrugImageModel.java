package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Id;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class AddDrugImageModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;
    private int type;

    protected AddDrugImageModel(String json, int type) {
        this.json = json;
        this.type = type;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.HR + basicModel.HR_ADD_DRUG + type;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("AddDrugImageModel", "url " + url);
        MyApplication.log("AddDrugImageModel", "session " + session);
        MyApplication.log("AddDrugImageModel", "session " + json);

        basicModel.requestServer.postApi(url, session, json, new ResponseHandle<RESP_Id>(RESP_Id.class) {
            @Override
            protected void onSuccess(RESP_Id obj) {
                AddDrugImageModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                AddDrugImageModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_Id resp_id);

    protected abstract void onError(int errorCode);
}