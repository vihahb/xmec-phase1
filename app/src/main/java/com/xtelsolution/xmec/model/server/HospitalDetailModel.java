package com.xtelsolution.xmec.model.server;

import android.util.Log;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_Hospital_Info;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 11/28/17
 * xtel.vn
 */

public abstract class HospitalDetailModel extends AbsICmd {
    private static final String TAG = "HospitalDetailModel";
    private BasicModel basicModel = new BasicModel();
    private  int id;

    public HospitalDetailModel(int id) {
        this.id = id;
        run();
    }

    @Override
    protected void invoke() {
//        http://x-mec.com/api/system/hospital/detail?id=528
        String url = basicModel.BASE_API
                + basicModel.HOSPITAL_DETAIL + id;
        Log.e(TAG, "url: " + url);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);
        basicModel.requestOkHttp.getApi(url, session, new ResponseHandle<RESP_Hospital_Info>(RESP_Hospital_Info.class) {
            @Override
            protected void onSuccess(RESP_Hospital_Info obj) {
                HospitalDetailModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        Log.e(TAG, "exception: " + message);
//        onErrror(ERROR_UNKOW);
    }

    protected abstract void onSuccess(RESP_Hospital_Info hospital_info);

    protected abstract void onErrror(int code);
}
