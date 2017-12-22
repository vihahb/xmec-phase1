package com.xtelsolution.xmec.presenter.hr;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveListModel;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.req.REQ_UserDrugs;
import com.xtelsolution.xmec.model.server.AddListDrugModel;
import com.xtelsolution.xmec.model.server.DeleteDrugModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hr.IAddDrugView;

import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/27/2017
 * Email: leconglongvu@gmail.com
 */
public class AddDrugPresenter extends BasicPresenter {
    private IAddDrugView view;

    public AddDrugPresenter(IAddDrugView view) {
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
                        view.onDeleteSuccess(userDrugs);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 2:

                REQ_UserDrugs reqDrugs = new REQ_UserDrugs();
                reqDrugs.getData().addAll(view.getListUserDrugs());

                new AddListDrugModel(JsonHelper.toJson(reqDrugs)) {
                    @Override
                    protected void onSuccess(List<UserDrugs> drugsList) {
//                        view.getHealthRecord().getUserDrugs().clear();
//                        view.getHealthRecord().getUserDrugs().addAll(drugsList);

                        saveListUserDrug(drugsList);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 3:

                int id3 = (int) params[1];
                final int type3 = (Integer) params[2];

                new GetObjectByKeyModel<UserDrugs>(UserDrugs.class, "id", id3) {
                    @Override
                    protected void onSuccess(UserDrugs object) {
                        if (object != null) {
                            view.onGetDrugScheduleSuccess(object, type3);
                        }
                    }
                };

                break;
        }
    }

    public void deleteDrug(UserDrugs userDrugs) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(1);
        iCmd(1, userDrugs);
    }

    private void saveListUserDrug(List<UserDrugs> drugsList) {
        RealmList<UserDrugs> realmList = new RealmList<>();
        realmList.addAll(drugsList);

        new SaveListModel<UserDrugs>(realmList) {
            @Override
            protected void onSuccess() {
                MyApplication.log("AddDrugPresenter", "saveListUserDrug success");
                view.onAddSuccess();
            }

            @Override
            protected void onError() {
                MyApplication.log("AddDrugPresenter", "saveListUserDrug error");
                view.onAddSuccess();
            }
        };
    }

    public void getUserDrug(int id, int type) {
        iCmd(3, id, type);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}
