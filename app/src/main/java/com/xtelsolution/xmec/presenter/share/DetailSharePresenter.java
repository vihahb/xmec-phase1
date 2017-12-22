package com.xtelsolution.xmec.presenter.share;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveListModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.resp.RESP_Mrb;
import com.xtelsolution.xmec.model.server.GetMbrByIdModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.share.IDetailShareView;

import io.realm.RealmList;

/**
 * Created by lecon on 12/6/2017
 */
public class DetailSharePresenter extends BasicPresenter {
    private IDetailShareView view;

    public DetailSharePresenter(IDetailShareView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 0:
                final int id0 = (int) params[1];

                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", id0) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        if (object != null)
                            view.onGetMbrSuccess(object);
                        else
                            iCmd(1, id0);
                    }
                };

                break;
            case 1:

                int id1 = (int) params[1];

                new GetMbrByIdModel(id1) {
                    @Override
                    protected void onSuccess(RealmList<Mbr> mbrList) {
                        if (mbrList.size() > 0) {
                            RESP_Mrb respMrb = new RESP_Mrb();
                            respMrb.setData(mbrList);
                            iCmd(2, respMrb);
                        } else {
                            view.onGetMbrSuccess(null);
                        }
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            view.onGetMbrSuccess(null);
                    }
                };

                break;
            case 2:

                final RESP_Mrb respMrb = (RESP_Mrb) params[1];
                respMrb.getData().get(0).setShareType(1);
                respMrb.getData().get(0).setShareTo(1);

                new SaveListModel<Mbr>(respMrb.getData()) {
                    @Override
                    protected void onSuccess() {
                        view.onGetMbrSuccess(respMrb.getData().get(0));
                    }

                    @Override
                    protected void onError() {
                        view.onGetMbrSuccess(null);
                    }
                };

                break;
        }
    }

    public void getMbr(int id) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.onErrorInternet();
            return;
        }

        view.showProgressBar();
        iCmd(0, id);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}