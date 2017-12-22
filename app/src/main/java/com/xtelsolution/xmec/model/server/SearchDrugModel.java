package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Drug;

import java.util.List;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SearchDrugModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String key;
    private int page;

    protected SearchDrugModel(String key, int page) {
        this.key = key;
        this.page = page;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API
                .concat(basicModel.HR)
                .concat(basicModel.DRUG)
                .concat(basicModel.HR_DRUG_SEARCH_KEY).concat(key)
                .concat(basicModel.HR_DRUG_PAGE)
                .concat(String.valueOf(page))
                .concat(basicModel.HR_DRUG_PAGE_SIZE)
                .concat(String.valueOf(Constants.DEFAULT_SIZE));
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("SearchDrug", "url " + url);
        MyApplication.log("SearchDrug", "session " + session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Drug>(RESP_Drug.class) {
            @Override
            protected void onSuccess(RESP_Drug obj) {
                SearchDrugModel.this.onSuccess(obj.getData());
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

    protected abstract void onSuccess(List<Drug> hospitals);

    protected abstract void onErrror(int code);
}