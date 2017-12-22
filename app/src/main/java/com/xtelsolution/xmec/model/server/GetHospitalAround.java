package com.xtelsolution.xmec.model.server;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.resp.RESP_Hospital;

import java.util.List;

/**
 * Created by ThanhChung on 08/11/2017.
 */

public abstract class GetHospitalAround extends AbsICmd {
    private static final String TAG = "GetHospitalAround";
    private BasicModel basicModel = new BasicModel();
    private LatLng latLng;
    private int type = 1;
    public GetHospitalAround(LatLng latLng, int type) {
        this.latLng = latLng;
        this.type = type;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API +
                String.format(basicModel.HOSPITAL_AROUND, latLng.latitude + "", latLng.longitude + "", type+"");
        Log.e(TAG, "invoke: " + url);
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_Hospital>(RESP_Hospital.class) {
            @Override
            protected void onSuccess(RESP_Hospital obj) {
                GetHospitalAround.this.onSuccess(obj.getData());
            }

            @Override
            protected void onError(Error error) {
                onErrror(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {

    }

    protected abstract void onSuccess(List<Hospital> hospitals);

    protected abstract void onErrror(int code);
}
