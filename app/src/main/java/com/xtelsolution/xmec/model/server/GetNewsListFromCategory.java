package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.resp.RESP_CategoryListInfo;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public abstract class GetNewsListFromCategory extends AbsICmd {

    private BasicModel basicModel = new BasicModel();
    private int id_category, page, pagesize;

    public GetNewsListFromCategory(int id_category, int page, int pagesize) {
        this.id_category = id_category;
        this.page = page;
        this.pagesize = pagesize;
        run();
    }

    @Override
    protected void invoke() {
        //http://x-mec.com/api/system/post/info/list?category_id=3&page=2&pagesize=10

        String url = basicModel.BASE_API + basicModel.SYSTEM
                + basicModel.POST_LIST_INFO
                + basicModel.CATEGORY_ID + id_category + "&page=" + page + "&pagesize=" + pagesize;

        MyApplication.log("url GetNewsListFromCategory", url);
        basicModel.requestOkHttp.getApi(url, null, new ResponseHandle<RESP_CategoryListInfo>(RESP_CategoryListInfo.class) {
            @Override
            protected void onSuccess(RESP_CategoryListInfo obj) {
                GetNewsListFromCategory.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_CategoryListInfo list);

    protected abstract void onErrror(int code);
}
