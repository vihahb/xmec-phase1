package com.xtelsolution.xmec.presenter.schedule;

import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.SaveDrugScheduleModel;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.server.CreateScheduleDrugModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.schedule.IAddDrugScheduleView;
import com.xtelsolution.xmec.view.activity.schedule.CreateScheduleActivity;
import com.xtelsolution.xmec.view.activity.schedule.UpdateScheduleActivity;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/15/2017
 * Email: leconglongvu@gmail.com
 */
public class AddDrugSchedulePresenter extends BasicPresenter {
    private IAddDrugScheduleView view;

    private RealmList<UserDrugSchedule> listDrug;

    public AddDrugSchedulePresenter(IAddDrugScheduleView view) {
        super(view);
        this.view = view;

        listDrug = new RealmList<>();
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                int hrID1 = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", hrID1) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        view.onGetListDrugSuccess(realmList);
                    }
                };

                break;
            case 2:

                int sdID2 = (int) params[1];

                new GetListByKeyModel<UserDrugSchedule>(UserDrugSchedule.class, "scheduleId", sdID2) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugSchedule> realmList) {
                        if (realmList != null)
                            listDrug.addAll(realmList);
                    }
                };

                break;
            case 3:

                final UserDrugSchedule userDrugSchedule3 = (UserDrugSchedule) params[1];

                new CreateScheduleDrugModel(JsonHelper.toJson(userDrugSchedule3)) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        userDrugSchedule3.setId(respId.getId());
                        iCmd(4, userDrugSchedule3);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
            case 4:

                final UserDrugSchedule userDrugSchedule4 = (UserDrugSchedule) params[1];

                new SaveDrugScheduleModel(userDrugSchedule4) {
                    @Override
                    protected void onSuccess() {
                        view.onAddDrugSuccess(userDrugSchedule4);
                    }

                    @Override
                    protected void onError() {
                        view.onAddDrugSuccess(userDrugSchedule4);
                    }
                };

                break;
        }
    }

    public void getData(int hrID) {
        iCmd(1, hrID);

        if (view.getScheduleDrugID() != -1)
            iCmd(2, view.getScheduleDrugID());
    }

    public void addDrugSchedule(Object... params) {
        String input = (String) params[0];
        UserDrugs userDrugs = (UserDrugs) params[1];
        int unit = (int) params[2];

        if (TextUtils.isEmpty(input)) {
            showError(301);
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(input);
        } catch (Exception e) {
            amount = -1;
        }

        if (amount == -1) {
            showError(302);
            return;
        } else if (amount == 0) {
            showError(301);
        }

        for (UserDrugSchedule userDrugSchedule : listDrug) {
            if (userDrugSchedule.getUserDrugId() == userDrugs.getId()) {
                showError(303);
                return;
            }
        }

        if (CreateScheduleActivity.scheduleDrug != null) {
            for (UserDrugSchedule userDrugSchedule : CreateScheduleActivity.scheduleDrug.getUserDrugSchedules()) {
                if (userDrugSchedule.getUserDrugId() == userDrugs.getId()) {
                    showError(304);
                    return;
                }
            }
        } else if (UpdateScheduleActivity.scheduleDrug != null) {
            for (UserDrugSchedule userDrugSchedule : UpdateScheduleActivity.scheduleDrug.getUserDrugSchedules()) {
                if (userDrugSchedule.getUserDrugId() == userDrugs.getId()) {
                    showError(304);
                    return;
                }
            }
        }

        UserDrugSchedule userDrugSchedule = new UserDrugSchedule();

        userDrugSchedule.setDrugName(userDrugs.getDrugName());
        userDrugSchedule.setAmount(amount);
        userDrugSchedule.setUserDrugId(userDrugs.getId());
        userDrugSchedule.setUnit(unit);
        userDrugSchedule.setScheduleId(view.getScheduleDrugID());

        if (view.getScheduleDrugID() != -1) {
            view.showProgressBar();
            iCmd(3, userDrugSchedule);
        } else {
            view.onAddDrugSuccess(userDrugSchedule);
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}