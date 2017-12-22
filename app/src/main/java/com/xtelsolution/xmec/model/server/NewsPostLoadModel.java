package com.xtelsolution.xmec.model.server;

import android.content.Context;
import android.util.Log;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.NewCategory;
import com.xtelsolution.xmec.model.resp.RESP_NewCategory;

import java.util.List;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public abstract class NewsPostLoadModel extends AbsICmd {
    private static final String TAG = "NewsPostLoadModel";
    private BasicModel basicModel = new BasicModel();


    private Context context;

    public NewsPostLoadModel(Context context) {
        this.context = context;
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API + basicModel.SYSTEM + basicModel.POST_LIST_CATEGORY;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);


        basicModel.requestServer.getApi(url, new ResponseHandle<RESP_NewCategory>(RESP_NewCategory.class) {
            @Override
            protected void onSuccess(RESP_NewCategory obj) {
                Log.e(TAG, "onSuccess: " + obj);

                NewsPostLoadModel.this.onSuccess(obj.getData());
            }

            @Override
            protected void onError(Error error) {
                NewsPostLoadModel.this.onError(error.getCode());
            }
        });

    }

    @Override
    protected void exception(String message) {

    }

    protected abstract void onSuccess(List<NewCategory> newsList);

    protected abstract void onError(int errorCode);


}
