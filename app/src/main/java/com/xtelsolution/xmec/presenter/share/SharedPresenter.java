package com.xtelsolution.xmec.presenter.share;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.req.REQ_Id;
import com.xtelsolution.xmec.model.resp.RESP_Mrb;
import com.xtelsolution.xmec.model.server.DeleteShareModel;
import com.xtelsolution.xmec.model.server.GetSharedAccountModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.share.ISharedView;

import io.realm.RealmList;

/**
 * Created by lecon on 12/1/2017
 */

public class SharedPresenter extends BasicPresenter {
    private ISharedView view;

    private Mbr mbr;

    public SharedPresenter(ISharedView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                new GetSharedAccountModel() {
                    @Override
                    protected void onSuccess(RESP_Mrb respShared) {
                        view.onGetSharedSuccess(respShared.getData());
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION) {
                            getNewSession(params);
                        } else
                            view.onGetSharedError(Constants.ERROR_UNKOW);
                    }
                };

                break;
            case 2:

                final REQ_Id reqId = new REQ_Id(mbr.getShareAccounts().get(0).getId());

                new DeleteShareModel(JsonHelper.toJson(reqId)) {
                    @Override
                    protected void onSuccess() {
                        mbr.getShareAccounts().remove(0);
                        iCmd(3, reqId.getId());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            showError(errorCode, params);
                        else
                            view.onDeleteErrpr(mbr);
                    }
                };

                break;
            case 3:

                int shareID = (int) params[1];

                new DeleteObjectByKeyModel<ShareAccounts>(ShareAccounts.class, "id", shareID) {
                    @Override
                    protected void onSuccess() {
                        checkDeleteMbr();
                    }

                    @Override
                    protected void onError() {
                        checkDeleteMbr();
                    }
                };

                break;
        }
    }

    public void getShared() {
        view.showProgressBar(1);

        if (!NetworkUtils.isConnected(view.getFragment().getContext())) {
            view.onNoInternet();
            return;
        }

        iCmd(1);
    }

    public void deleteMbr(Mbr mbr) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(2);
        this.mbr = mbr;
        checkDeleteMbr();
    }

    public void checkDeleteMbr() {
        if (mbr.getShareAccounts().size() == 0) {
            view.onDeleteSuccess(mbr);
            return;
        }

        iCmd(2);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}