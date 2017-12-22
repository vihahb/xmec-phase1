package com.xtelsolution.xmec.model.server;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_CategoryList;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */

public abstract class GetCategoryNewsfeed extends AbsICmd {

    private BasicModel basicModel = new BasicModel();

    public GetCategoryNewsfeed() {
        run();
    }

    @Override
    protected void invoke() {

        String url = basicModel.BASE_API + basicModel.SYSTEM
                + basicModel.POST_LIST_CATEGORY;

        basicModel.requestOkHttp.getApi(url, null, new ResponseHandle<RESP_CategoryList>(RESP_CategoryList.class) {
            @Override
            protected void onSuccess(RESP_CategoryList obj) {
                GetCategoryNewsfeed.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                GetCategoryNewsfeed.this.onErrror(error.getCode());
            }
        });

    }

    @Override
    protected void exception(String message) {
        onErrror(ERROR_UNKOW);
    }

    protected abstract void onSuccess(RESP_CategoryList object);

    protected abstract void onErrror(int code);
}
