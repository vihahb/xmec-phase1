package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Disease;

import java.util.List;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 11/22/17.
 */

public abstract class DiseaseSearchKey extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String key;
    private int page;
    private int size;

    public DiseaseSearchKey(String key, int page, int size) {

        this.key = key;
        this.page = page;
        this.size = size;
        run();
    }

    public DiseaseSearchKey(String key, int page) {
        this.key = key;
        this.page = page;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.SYSTEM +
                basicModel.HR_D_SEARCH_KEY + key +
                basicModel.HR_D_SEARCH_PAGE + page +
                basicModel.HR_D_SEARCH_SIZE + size;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("SearchDiseasesModel", "url " + url);
        MyApplication.log("SearchDiseasesModel", "session " + session);

        basicModel.requestOkHttp.getApi(url, session, new ResponseHandle<RESP_Disease>(RESP_Disease.class) {
            @Override
            protected void onSuccess(RESP_Disease obj) {
                MyApplication.log("SearchDiseasesModel", obj.toString());
                DiseaseSearchKey.this.onSuccess(obj.getData());
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

    protected abstract void onSuccess(List<DiseaseObject> diseases);

    protected abstract void onErrror(int code);
}
