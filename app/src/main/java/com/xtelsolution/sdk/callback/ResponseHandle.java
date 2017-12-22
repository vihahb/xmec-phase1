package com.xtelsolution.sdk.callback;

import android.util.Log;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Created by Lê Công Long Vũ on 12/4/2016
 */

public abstract class ResponseHandle<T extends RESP_Basic> {
    private Class<T> clazz;
    private static final String TAG = "ResponseHandle";

    protected ResponseHandle(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void onSuccess(String result) {


        try {
            Log.e(TAG, "result: " + result);
            if (TextUtils.isEmpty(result)) {
                onSuccess((T) null);
            } else {
                T t = JsonHelper.getObjectNoException(result, clazz);
                if (t.getError() != null && t.getError().getCode() != 0) {
                    onError(t.getError());
                } else {
                    onSuccess(t);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            onError(new Error(Constants.ERROR_UNKOW, "ERROR_PARSER_RESPONSE", "ERROR"));
        }
    }

    public void onError(Exception error) {
        onError(new Error(Constants.ERROR_UNKOW, "ERROR_REQUEST_RESPONSE", error.getMessage()));
    }

    protected abstract void onSuccess(T obj);

    protected abstract void onError(Error error);
}
