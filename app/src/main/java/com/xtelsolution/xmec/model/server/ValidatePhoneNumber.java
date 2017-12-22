package com.xtelsolution.xmec.model.server;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.resp.RESP_ValidatePhone;

/**
 * Created by vivu on 12/16/17
 * xtel.vn
 */

public abstract class ValidatePhoneNumber extends AbsICmd {
    private BasicModel basicModel = new BasicModel();
    String number;

    public ValidatePhoneNumber(String number) {
        this.number = number;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.SYSTEM + basicModel.VALIDATE_PHONE + number;
        basicModel.requestOkHttp.getApi(url, null, new ResponseHandle<RESP_ValidatePhone>(RESP_ValidatePhone.class) {
            @Override
            protected void onSuccess(RESP_ValidatePhone obj) {
                ValidatePhoneNumber.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                ValidatePhoneNumber.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_ValidatePhone validatePhone);
    protected abstract void onError(int errorCode);
}
