package com.xtelsolution.xmec.presenter.schedule;

import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.server.UpdateDrugScheduleModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.schedule.IUpdateDrugScheduleView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/15/2017
 * Email: leconglongvu@gmail.com
 */
public class UpdateDrugSchedulePresenter extends BasicPresenter {
    private IUpdateDrugScheduleView view;

    private RealmList<UserDrugSchedule> listDrugSchedule;

    public UpdateDrugSchedulePresenter(IUpdateDrugScheduleView view) {
        super(view);
        this.view = view;
        listDrugSchedule = new RealmList<>();
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                final int id = (int) params[1];

                new GetObjectByKeyModel<UserDrugSchedule>(UserDrugSchedule.class, "id", id) {
                    @Override
                    protected void onSuccess(UserDrugSchedule object) {
                        view.onGetDrugSuccess(object);
                        iCmd(2, object);
                    }
                };

                break;
            case 2:

                final UserDrugSchedule userDrugSchedule2 = (UserDrugSchedule) params[1];

                new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", userDrugSchedule2.getScheduleId()) {
                    @Override
                    protected void onSuccess(ScheduleDrug object) {
                        if (object != null) {
                            iCmd(3, object.getHealthRecordId());
                            listDrugSchedule.addAll(object.getUserDrugSchedules());
                        } else
                            view.onGetDrugSuccess(null);
                    }
                };

                break;
            case 3:

                int id3 = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", id3) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        view.onGetListDrugSuccess(realmList);
                    }
                };

                break;
            case 4:

                new UpdateDrugScheduleModel(JsonHelper.toJson(view.getDrugSchedule())) {
                    @Override
                    protected void onSuccess() {
                        iCmd(5);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
            case 5:

                new SaveObjectModel<UserDrugSchedule>(view.getDrugSchedule()) {
                    @Override
                    protected void onSuccess() {
                        view.onUpdateSuccess();
                    }

                    @Override
                    protected void onError() {
                        view.onUpdateSuccess();
                    }
                };

                break;
        }
    }

    public void getData(int id) {
        iCmd(1, id);
    }

    public void updateDrug(Object... params) {
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

        for (UserDrugSchedule userDrugSchedule : listDrugSchedule) {
            if (userDrugSchedule.getUserDrugId() == userDrugs.getId() && userDrugs.getId() != view.getDrugSchedule().getUserDrugId()) {
                showError(303);
                return;
            }
        }

        view.getDrugSchedule().setDrugName(userDrugs.getDrugName());
        view.getDrugSchedule().setAmount(amount);
        view.getDrugSchedule().setUserDrugId(userDrugs.getId());
        view.getDrugSchedule().setUnit(unit);

        view.showProgressBar();

        iCmd(4);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}