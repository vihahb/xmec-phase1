package com.xtelsolution.xmec.presenter.mbr;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.DeleteOlDataaModel;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetListMbrModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveListModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.NotificationAction;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.req.REQ_Id;
import com.xtelsolution.xmec.model.resp.RESP_Mrb;
import com.xtelsolution.xmec.model.server.DeleteHRModel;
import com.xtelsolution.xmec.model.server.GetMbrsModel;
import com.xtelsolution.xmec.model.server.UpdateHrContinueModel;
import com.xtelsolution.xmec.model.server.UpdateHrDoneModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.fragment.inf.mbr.IMbrView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/19/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrPresenter extends BasicPresenter {
    private IMbrView view;

    public MbrPresenter(IMbrView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 0:

                new GetListMbrModel() {
                    @Override
                    protected void onSuccess(RealmList<Mbr> realmList) {
                        if (realmList != null) {
                            view.onGetSuccess(realmList);
                        } else {
                            view.onGetMbrError(Constants.ERROR_NO_INTERNET);
                        }
                    }
                };

                break;
            case 1:

                new GetMbrsModel() {
                    @Override
                    protected void onSuccess(RESP_Mrb respMrb) {
//                        if (view.getFragment() != null) {
                            iCmd(2, respMrb);
//                        }
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (view.getFragment() != null) {
                            if (code == Constants.ERROR_EMPTY)
                                view.onGetSuccess(new ArrayList<Mbr>());
                            else {
                                showError(code, params);
                                view.onGetMbrError(code);
                            }
                        }
                    }
                };

                break;
            case 2:

                final RESP_Mrb respMrb2 = (RESP_Mrb) params[1];

                new DeleteOlDataaModel() {
                    @Override
                    protected void onSuccess() {
                        iCmd(3, respMrb2);
                    }

                    @Override
                    protected void onError() {
                        if (view.getFragment() != null) {
                            view.onGetMbrError(Constants.ERROR_UNKOW);
                        }
                    }
                };

                break;
            case 3:

                final RESP_Mrb respMrb3 = (RESP_Mrb) params[1];

                new SaveListModel<Mbr>(respMrb3.getData()) {
                    @Override
                    protected void onSuccess() {
                        MyApplication.log("ok", "ok");
                        sortMbr(respMrb3);
                    }

                    @Override
                    protected void onError() {
                        if (view.getFragment() != null) {
                            MyApplication.log("onError", "onError");
                            view.onGetMbrError(Constants.ERROR_UNKOW);
                        }
                    }
                };

                break;
            case 4:

                int mbrID3 = (int) params[1];

                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbrID3) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        if (object != null && view.getFragment() != null)
                            view.onAddMbrSuccess(object);
                    }
                };

                break;
            case 5:

                int hrID3 = (int) params[1];
                final int type3 = (int) params[2];

                new GetObjectByKeyModel<HealthRecords>(HealthRecords.class, "id", hrID3) {
                    @Override
                    protected void onSuccess(HealthRecords object) {
                        if (object != null && view.getFragment() != null) {
                            if (type3 == 1)
                                view.onCreatedHealthRecord(object);
                            else
                                view.onUpdatedHealthRecord(object);
                        }
                    }
                };

                break;
            case 6:

                final HealthRecords healthRecords6 = (HealthRecords) params[1];

                REQ_Id req_id = new REQ_Id(healthRecords6.getId());

                new UpdateHrDoneModel(JsonHelper.toJson(req_id)) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null) {
                            healthRecords6.setEndDateLong(System.currentTimeMillis());
                            iCmd(7, healthRecords6);
                        }
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (view.getFragment() != null)
                            showError(errorCode);
                    }
                };

                break;
            case 7:

                final HealthRecords healthRecords7 = (HealthRecords) params[1];

                for (int i = healthRecords7.getScheduleDrugs().size() - 1; i >= 0; i--) {
                    healthRecords7.getScheduleDrugs().get(i).setState(2);
                }

                new SaveObjectModel<HealthRecords>(healthRecords7) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null)
                            iCmd(8, healthRecords7);
                    }

                    @Override
                    protected void onError() {
                        if (view.getFragment() != null)
                            iCmd(8, healthRecords7);
                    }
                };

                break;
            case 8:

                final HealthRecords healthRecords8 = (HealthRecords) params[1];

                for (ScheduleDrug scheduleDrug : healthRecords8.getScheduleDrugs()) {
                    ReminderUtils.cancelReminderSchedule(view.getFragment().getContext(), scheduleDrug.getId());
                }

                view.onEndOfTreatmentSuccess(healthRecords8);

                break;
            case 9:

                final HealthRecords healthRecords9 = (HealthRecords) params[1];

                new DeleteHRModel(healthRecords9.getId()) {
                    @Override
                    protected void onSuccess(RESP_Basic obj) {
                        if (view.getFragment() != null)
                            iCmd(10, healthRecords9);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (view.getFragment() != null)
                            showError(errorCode, params);
                    }
                };

                break;
            case 10:

                final HealthRecords healthRecords10 = (HealthRecords) params[1];

                new DeleteObjectByKeyModel<HealthRecords>(HealthRecords.class, "id", healthRecords10.getId()) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null)
                            view.onDeleteHRSuccess(healthRecords10);
                    }

                    @Override
                    protected void onError() {
                        if (view.getFragment() != null)
                            view.onDeleteHRSuccess(healthRecords10);
                    }
                };

                break;
            case 11:

                final HealthRecords healthRecords11 = (HealthRecords) params[1];

                REQ_Id req_id11 = new REQ_Id(healthRecords11.getId());

                new UpdateHrContinueModel(JsonHelper.toJson(req_id11)) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null) {
                            healthRecords11.setEndDateLong(0);
                            iCmd(12, healthRecords11);
                        }
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (view.getFragment() != null)
                            showError(errorCode);
                    }
                };

                break;
            case 12:

                final HealthRecords healthRecords12 = (HealthRecords) params[1];

                new SaveObjectModel<HealthRecords>(healthRecords12) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null)
                            view.onContinueTreatmentSuccess(healthRecords12);
                    }

                    @Override
                    protected void onError() {
                        if (view.getFragment() != null)
                            view.onContinueTreatmentSuccess(healthRecords12);
                    }
                };

                break;
            case 13:

                int shareID = (int) params[1];

                new DeleteObjectByKeyModel<ShareAccounts>(ShareAccounts.class, "id", shareID) {
                    @Override
                    protected void onSuccess() {
                        if (view.getFragment() != null)
                            view.onDeleteShareSuccess();
                    }

                    @Override
                    protected void onError() {

                    }
                };

                break;
        }
    }

    public void getMbrs() {
        if (!NetworkUtils.isConnected(view.getFragment().getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            iCmd(0);
            return;
        }

        view.showProgressBar(1);
        iCmd(1);
    }

    public void getMbrsAgain() {
        if (view.getFragment() != null && view.getFragment().getActivity() != null)
            if (!NetworkUtils.isConnected(view.getFragment().getActivity())) {
                showError(300);
                return;
            }

        iCmd(1);
    }

    public void continueTreatment(HealthRecords healthRecords) {
        if (!NetworkUtils.isConnected(view.getFragment().getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(4);
        iCmd(11, healthRecords);
    }

    public void endOfTreatment(HealthRecords healthRecords) {
        if (!NetworkUtils.isConnected(view.getFragment().getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(2);
        iCmd(6, healthRecords);
    }

    public void deleteHealthRecord(HealthRecords healthRecords) {
        if (!NetworkUtils.isConnected(view.getFragment().getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(3);
        iCmd(9, healthRecords);
    }

    private void sortMbr(RESP_Mrb respMrb) {
        for (int i = respMrb.getData().size() - 1; i >= 0; i--) {
            if (respMrb.getData().get(i).getHealthRecords() != null) {
                Collections.sort(respMrb.getData().get(i).getHealthRecords(), new Comparator<HealthRecords>() {
                    @Override
                    public int compare(HealthRecords lhs, HealthRecords rhs) {
                        try {
                            return (int) (rhs.getDateCreateLong() - lhs.getDateCreateLong());
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
            }
        }

        view.onGetSuccess(respMrb.getData());
    }

    public void getMbrAdded(int mbrID) {
        iCmd(4, mbrID);
    }

    public void getHealthRecord(int hrID, int type) {
        iCmd(5, hrID, type);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public void deleteShareAccount(int shareId) {
        iCmd(13, shareId);
    }
}
