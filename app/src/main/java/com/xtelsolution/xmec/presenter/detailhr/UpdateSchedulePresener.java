package com.xtelsolution.xmec.presenter.detailhr;

import android.app.AlarmManager;
import android.util.SparseBooleanArray;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Update_Schedule;
import com.xtelsolution.xmec.model.server.AddTimePerDayModel;
import com.xtelsolution.xmec.model.server.DeleteDrugScheduleModel;
import com.xtelsolution.xmec.model.server.DeleteTimePerDayModel;
import com.xtelsolution.xmec.model.server.UpdateScheduleModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.activity.inf.schedule.IUpdateScheduleView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class UpdateSchedulePresener extends BasicPresenter {
    private IUpdateScheduleView view;

    private SparseBooleanArray arrayShare;

    private RealmList<ScheduleTimePerDay> realmList;

    public UpdateSchedulePresener(IUpdateScheduleView view) {
        super(view);
        this.view = view;

        arrayShare = new SparseBooleanArray();
        realmList = new RealmList<>();
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 0:

                int hrID = (int) params[1];

                new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", hrID) {
                    @Override
                    protected void onSuccess(ScheduleDrug object) {
                        view.onGetScheduleSuccess(object);

                        if (object != null)
                            iCmd(1, object.getHealthRecordId());
                    }
                };

                break;
            case 1:

                int hrID0 = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", hrID0) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        if (realmList != null) {
                            view.onGetListDrugSuccess(realmList);
                        }
                    }
                };

                break;
            case 2:

                final UserDrugSchedule userDrugSchedule3 = (UserDrugSchedule) params[1];

                new DeleteDrugScheduleModel(JsonHelper.toJson(userDrugSchedule3)) {
                    @Override
                    protected void onSuccess() {
                        iCmd(3, userDrugSchedule3);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
            case 3:

                final UserDrugSchedule userDrugSchedule4 = (UserDrugSchedule) params[1];

                new DeleteObjectByKeyModel<UserDrugSchedule>(UserDrugSchedule.class, "id", userDrugSchedule4.getId()) {
                    @Override
                    protected void onSuccess() {
                        view.onDeleteDrugUserSuccess(userDrugSchedule4);
                    }

                    @Override
                    protected void onError() {
                        view.onDeleteDrugUserSuccess(userDrugSchedule4);
                    }
                };

                break;
            case 4:

                int id5 = (int) params[1];
                final int type5 = (int) params[2];

                new GetObjectByKeyModel<UserDrugSchedule>(UserDrugSchedule.class, "id", id5) {
                    @Override
                    protected void onSuccess(UserDrugSchedule object) {
                        if (object != null)
                            view.onGetDrugScheduleSuccess(object, type5);
                    }
                };

                break;

            case 5:

                new UpdateScheduleModel(JsonHelper.toJson(view.getScheduleDrug())) {
                    @Override
                    protected void onSuccess(RESP_Update_Schedule obj) {
//                        ScheduleDrug scheduleDrug = new ScheduleDrug(obj);
                        iCmd(6);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
            case 6:

                new SaveObjectModel<ScheduleDrug>(view.getScheduleDrug()) {
                    @Override
                    protected void onSuccess() {
                        checkTimePerDay();
                    }

                    @Override
                    protected void onError() {
                        checkTimePerDay();
                    }
                };

                break;
            case 7:

                final ScheduleTimePerDay scheduleTimePerDay7 = view.getScheduleDrug().getScheduleTimePerDays().get(0);

                new DeleteTimePerDayModel(scheduleTimePerDay7.getId()) {
                    @Override
                    protected void onSuccess() {
                        view.getScheduleDrug().getScheduleTimePerDays().remove(0);
                        iCmd(8, scheduleTimePerDay7.getId());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(305);
                    }
                };

                break;
            case 8:

                int id8 = (int) params[1];

                new DeleteObjectByKeyModel<ScheduleTimePerDay>(ScheduleTimePerDay.class, "id", id8) {
                    @Override
                    protected void onSuccess() {
                        checkDeleteTimePerDay();
                    }

                    @Override
                    protected void onError() {
                        checkDeleteTimePerDay();
                    }
                };

                break;
            case 9:

                final ScheduleTimePerDay scheduleTimePerDay9 = view.getTimePerDay().get(0);
                scheduleTimePerDay9.setScheduleDrugId(view.getScheduleDrug().getId());

                new AddTimePerDayModel(JsonHelper.toJson(scheduleTimePerDay9)) {
                    @Override
                    protected void onSuccess(RESP_Id respId) {
                        view.getTimePerDay().remove(0);
                        scheduleTimePerDay9.setId(respId.getId());
                        realmList.add(scheduleTimePerDay9);
                        checkAddTimePerDay();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(305);
                    }
                };

                break;
            case 10:

                view.getScheduleDrug().setScheduleTimePerDays(realmList);

                new SaveObjectModel<ScheduleDrug>(view.getScheduleDrug()) {
                    @Override
                    protected void onSuccess() {
                        ReminderUtils.startService(view.getActivity(), view.getScheduleDrug().getId());
                        view.onUpdateScheduleSuccess();
                    }

                    @Override
                    protected void onError() {
                        ReminderUtils.startService(view.getActivity(), view.getScheduleDrug().getId());
                        view.onUpdateScheduleSuccess();
                    }
                };

                break;
        }
    }

    public void getData(int hrID) {
        iCmd(0, hrID);
    }

    public void getDrugSchedule(int id, int type) {
        iCmd(4, id, type);
    }

    public void updateSchedule() {
        if (TextUtils.isEmpty(view.getScheduleDrug().getName())) {
            showError(301);
            return;
        }

        if (view.getScheduleDrug().getDateCreateLong() == 0) {
            showError(302);
            return;
        }

        for (ScheduleTimePerDay scheduleTimePerDay : view.getScheduleDrug().getScheduleTimePerDays()) {
            if (scheduleTimePerDay.getScheduleType() == 1) {
                if (scheduleTimePerDay.getStartTime() == null) {
                    showError(303);
                    return;
                }
            } else {
                if (scheduleTimePerDay.getStartTime() == null || scheduleTimePerDay.getEndTime() == null) {
                    showError(303);
                    return;
                }
            }
        }

        for (ScheduleTimePerDay scheduleTimePerDay : view.getTimePerDay()) {
            if (scheduleTimePerDay.getScheduleType() == 1) {
                if (scheduleTimePerDay.getStartTime() == null) {
                    showError(303);
                    return;
                }
            } else {
                if (scheduleTimePerDay.getStartTime() == null || scheduleTimePerDay.getEndTime() == null) {
                    showError(303);
                    return;
                }
            }
        }

        if (view.getTimePerDay().get(0).getScheduleType() == 1) {
            if (view.getTimePerDay().size() > 0) {
                for (int i = view.getTimePerDay().size() - 1; i > 0; i--) {
                    if (view.getTimePerDay().get(i).getStartTime().equals(view.getTimePerDay().get((i - 1)).getStartTime())) {
                        showError(304);
                        return;
                    }
                }
            }
        } else {
            ScheduleTimePerDay scheduleTimePerDay = view.getTimePerDay().get(0);

            long time = TimeUtils.convertTimeToLong(scheduleTimePerDay.getEndTime()) - TimeUtils.convertTimeToLong(scheduleTimePerDay.getStartTime());

            if ((time / AlarmManager.INTERVAL_HOUR) < scheduleTimePerDay.getTime()) {
                showError(304);
                return;
            }
        }

        view.showProgressBar(1);

        iCmd(5);
    }

    public void deleteDrugSchedule(UserDrugSchedule userDrugSchedule) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(2);
        iCmd(2, userDrugSchedule);
    }

    private void checkTimePerDay() {
        for (ScheduleTimePerDay scheduleTimePerDay : view.getTimePerDay()) {

            if (scheduleTimePerDay.getId() != -1) {
                arrayShare.put(scheduleTimePerDay.getId(), true);
            }
        }

        for (int i = view.getScheduleDrug().getScheduleTimePerDays().size() - 1; i >= 0; i--) {
            ScheduleTimePerDay scheduleTimePerDay = view.getScheduleDrug().getScheduleTimePerDays().get(i);

            if (arrayShare.get(scheduleTimePerDay.getId())) {
                realmList.add(0, scheduleTimePerDay);
                view.getScheduleDrug().getScheduleTimePerDays().remove(scheduleTimePerDay);
            }
        }

        checkDeleteTimePerDay();
    }

    public void checkDeleteTimePerDay() {
        if (view.getScheduleDrug().getScheduleTimePerDays().size() == 0) {

            for (int i = view.getTimePerDay().size() - 1; i >= 0; i--) {
                ScheduleTimePerDay scheduleTimePerDay = view.getTimePerDay().get(i);

                if (arrayShare.get(scheduleTimePerDay.getId())) {
                    view.getTimePerDay().remove(i);
                }
            }

            checkAddTimePerDay();
            return;
        }

        iCmd(7);
    }

    private void checkAddTimePerDay() {
        if (view.getTimePerDay().size() == 0) {
            iCmd(10);
            return;
        }

        iCmd(9);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}