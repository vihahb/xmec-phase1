package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.resp.RESP_User_Diseases;

import java.util.List;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SearchDiseasesModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();

    private String key;
    private int page;
    private int size;

    protected SearchDiseasesModel(String key, int page) {
        this.key = key;
        this.page = page;
        this.size = Constants.DEFAULT_SIZE;

        run();
    }

    protected SearchDiseasesModel(String key, int page, int size) {
        this.key = key;
        this.page = page;
        this.size = size;

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

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_User_Diseases>(RESP_User_Diseases.class) {
            @Override
            protected void onSuccess(RESP_User_Diseases obj) {
                SearchDiseasesModel.this.onSuccess(obj.getData());
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

    protected abstract void onSuccess(List<UserDiseases> diseases);

    protected abstract void onErrror(int code);
}