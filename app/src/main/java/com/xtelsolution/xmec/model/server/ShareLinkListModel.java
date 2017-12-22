package com.xtelsolution.xmec.model.server;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_ListShareLinkMbr;

/**
 * Created by xtel on 11/14/17
 */

public abstract class ShareLinkListModel extends AbsICmd {

    private static final String TAG = "ShareLinkListModel";

    private BasicModel basicModel = new BasicModel();

    private int type, mbr_id;

    public ShareLinkListModel(int type, int mbr_id) {
        this.type = type;
        this.mbr_id = mbr_id;
        run();
    }

    @Override
    protected void invoke() {

        String url = basicModel.BASE_API
                + basicModel.MBR
                + basicModel.MBR_SHARE
                + basicModel.MBR_USER
                + basicModel.MBR_TYPE + type
                + basicModel.MBR_ID + mbr_id;
        String session = SharedUtils.getInstance().getStringValue(Constants.SESSION);

        MyApplication.log(TAG + " url ", url);
        MyApplication.log(TAG + " session ", session);

        basicModel.requestServer.getApi(url, session, new ResponseHandle<RESP_ListShareLinkMbr>(RESP_ListShareLinkMbr.class) {
            @Override
            protected void onSuccess(RESP_ListShareLinkMbr obj) {
                ShareLinkListModel.this.onSuccess(obj);
            }

            @Override
            protected void onError(Error error) {
                ShareLinkListModel.this.onError(error.getCode());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(-1);
    }

    protected abstract void onSuccess(RESP_ListShareLinkMbr obj);

    protected abstract void onError(int errorCode);
}
