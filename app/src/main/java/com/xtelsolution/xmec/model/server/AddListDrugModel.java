package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.resp.RESP_UserDrugs;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class AddListDrugModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String json;

    protected AddListDrugModel(String json) {
        this.json = json;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.HR + basicModel.DRUG + basicModel.LIST;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("AddListDrugModel", "url " + url);
        MyApplication.log("AddListDrugModel", "session " + session);
        MyApplication.log("AddListDrugModel", "session " + json);

        basicModel.requestServer.postApi(url, session, json, new ResponseHandle<RESP_UserDrugs>(RESP_UserDrugs.class) {
            @Override
            protected void onSuccess(RESP_UserDrugs obj) {
                AddListDrugModel.this.onSuccess(obj.getData());
            }

            @Override
            protected void onError(Error error) {
                AddListDrugModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(List<UserDrugs> drugsList);

    protected abstract void onError(int errorCode);
}