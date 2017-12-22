package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_ListRate;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 11/30/17
 * xtel.vn
 */

public abstract class GetRateHospital extends AbsICmd {

    long hospital_id;
    private BasicModel basicModel = new BasicModel();
    private int page, page_size;

    public GetRateHospital(long hospital_id, int page, int page_size) {
        this.hospital_id = hospital_id;
        this.page = page;
        this.page_size = page_size;
        run();
    }

    @Override
    protected void invoke() {
        //http://x-mec.com/api/hospital/detail/rate?id=528&page=1&pagesize=10
        String url = basicModel.BASE_API
                + basicModel.HOSPITAL_GET_RATE + hospital_id + "&page=" + page + "&pagesize=" + page_size;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log("GetRateHospital", url);
        MyApplication.log("GetRateHospital", session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_ListRate>(RESP_ListRate.class) {
            @Override
            protected void onSuccess(RESP_ListRate obj) {
                GetRateHospital.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_ListRate listRate);

    protected abstract void onErrror(int code);
}
