package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_SearchDrug;

import static com.xtelsolution.sdk.commons.Constants.ERROR_UNKOW;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public abstract class GetDrugListSearch extends AbsICmd {

    private BasicModel basicModel = new BasicModel();
    String key;
    private int page, page_size;

    public GetDrugListSearch(String key, int page, int pagesize) {
        this.key = key;
        this.page = page;
        this.page_size = pagesize;
        run();
    }

    @Override
    protected void invoke() {
        String url = basicModel.BASE_API +
                basicModel.SYSTEM +
                basicModel.DRUG +
                basicModel.DRUG_SEARCH + key + "&page=" + page + "&pagesize=" + page_size;

        MyApplication.log("GetDrugListSearch", url);
        basicModel.requestOkHttp.getApi(url, null, new ResponseHandle<RESP_SearchDrug>(RESP_SearchDrug.class) {
            @Override
            protected void onSuccess(RESP_SearchDrug obj) {
                GetDrugListSearch.this.onSuccess(obj);
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

    protected abstract void onSuccess(RESP_SearchDrug searchDrug);

    protected abstract void onErrror(int code);
}
