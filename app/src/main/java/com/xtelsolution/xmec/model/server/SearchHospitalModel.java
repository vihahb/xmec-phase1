package com.xtelsolution.xmec.model.server;

import android.util.Log;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.resp.RESP_Hospital;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SearchHospitalModel extends AbsICmd {
    private BasicModel basicModel = new BasicModel();
    private static final String TAG = "SearchHospitalModel";
    private String key;
    private int page;

    protected SearchHospitalModel(String key, int page) {
        this.key = key;
        this.page = page;

        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API
                .concat(basicModel.HOSPITAL_SEARCH_KEY)
                .concat(key).concat(basicModel.HOSPITAL_SEARCH_PAGE)
                .concat(String.valueOf(page))
                .concat(basicModel.HOSPITAL_SEARCH_SIZE)
                .concat(String.valueOf(Constants.DEFAULT_SIZE));
        Log.e(TAG, "invoke: " + url);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Hospital>(RESP_Hospital.class) {
            @Override
            protected void onSuccess(RESP_Hospital obj) {
                SearchHospitalModel.this.onSuccess(obj.getData());
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

    protected abstract void onSuccess(List<Hospital> hospitals);

    protected abstract void onErrror(int code);
}