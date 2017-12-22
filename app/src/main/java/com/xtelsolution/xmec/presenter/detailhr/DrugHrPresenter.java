package com.xtelsolution.xmec.presenter.detailhr;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.server.DeleteDrugModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.IDrugHrView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/1/2017
 * Email: leconglongvu@gmail.com
 */
public class DrugHrPresenter extends BasicPresenter {
    private IDrugHrView view;

    public DrugHrPresenter(IDrugHrView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                final UserDrugs userDrugs = (UserDrugs) params[1];

                new DeleteDrugModel(userDrugs.getId()) {
                    @Override
                    protected void onSuccess() {
                        iCmd(4, userDrugs);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 2:

                int hrId = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class,"healthRecordId", hrId) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        view.onGetUserDrugsSuccess(realmList);
                    }
                };

                break;
            case 3:

                int id3 = (int) params[1];
                final int type3 = (int) params[2];

                new GetObjectByKeyModel<UserDrugs>(UserDrugs.class, "id", id3) {
                    @Override
                    protected void onSuccess(UserDrugs object) {
                        if (object != null) {
                            if (type3 == 1) {
                                view.onCreatedDrug(object);
                            } else {
                                view.onUpdatedDrug(object);
                            }
                        }
                    }
                };

                break;
            case 4:

                final UserDrugs userDrugs4 = (UserDrugs) params[1];

                new DeleteObjectByKeyModel<UserDrugs>(UserDrugs.class, "id", userDrugs4.getId()) {
                    @Override
                    protected void onSuccess() {
                        view.onDeleteSuccess(userDrugs4);
                    }

                    @Override
                    protected void onError() {
                        view.onDeleteSuccess(userDrugs4);
                    }
                };

                break;
        }
    }

    public void deleteDrug(UserDrugs userDrugs) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext().getApplicationContext())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(1);
        iCmd(1, userDrugs);
    }

    public void getUserDrugs(int hrId) {
        iCmd(2, hrId);
    }

    public void getDrugRequest(int id, int type) {
        iCmd(3, id, type);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}