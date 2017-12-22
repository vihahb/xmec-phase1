package com.xtelsolution.xmec.presenter.share;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.database.SaveListModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.req.REQ_Id;
import com.xtelsolution.xmec.model.resp.RESP_Mrb;
import com.xtelsolution.xmec.model.server.DeleteSharedModel;
import com.xtelsolution.xmec.model.server.GetBeSharedModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.share.IBeSharedView;

/**
 * Created by lecon on 12/1/2017
 */

public class BeSharedPresenter extends BasicPresenter {
    private IBeSharedView view;

    public BeSharedPresenter(IBeSharedView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                new GetBeSharedModel() {
                    @Override
                    protected void onSuccess(RESP_Mrb respShared) {
                        if (view.getFragment() != null) {
                            view.onGetSharedSuccess(respShared.getData());
                        }
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (view.getFragment() != null) {
                            if (code == Constants.ERROR_SESSION) {
                                getNewSession(params);
                            } else
                                view.onGetSharedError(Constants.ERROR_UNKOW);
                        }
                    }
                };

                break;
            case 3:

                final Mbr shared = (Mbr) params[1];
                REQ_Id reqId = new REQ_Id();

                int uid = SharedUtils.getInstance().getIntValue(Constants.USER_UID);

                for (ShareAccounts shareAccounts : shared.getShareAccounts()) {
                    if (shareAccounts.getUid() == uid) {
                        reqId.setId(shareAccounts.getId());
                    }
                }

                new DeleteSharedModel(JsonHelper.toJson(reqId)) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null) {
                            view.onDeleteSuccess(shared);
                        }
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (view.getFragment() != null) {
                            if (errorCode == Constants.ERROR_SESSION) {
                                getSessionSuccess(params);
                            } else {
                                view.onDeleteError();
                            }
                        }
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

    public void deleteBeShared(Mbr item) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        if (item.getShareAccounts().size() == 0) {
            showError(Constants.ERROR_UNKOW);
            return;
        }

        view.showProgressBar(2);

        iCmd(3, item);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}