package com.xtelsolution.xmec.presenter.detailhr;

import android.app.AlarmManager;

import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveScheduleModel;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.resp.RESP_Schedule_Drug;
import com.xtelsolution.xmec.model.server.AddListScheduleModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.activity.inf.schedule.ICreateScheduleView;

import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class CreateSchedulePresener extends BasicPresenter {
    private ICreateScheduleView view;

    private HealthRecords healthRecords;

    public CreateSchedulePresener(ICreateScheduleView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case -1:

                int hrID = (int) params[1];

                new GetObjectByKeyModel<HealthRecords>(HealthRecords.class, "id", hrID) {
                    @Override
                    protected void onSuccess(HealthRecords object) {
                        if (object != null) {
                            healthRecords = object;
                            view.onGetDataSuccess(true);
                        } else {
                            view.onGetDataSuccess(false);
                        }
                    }
                };

                break;
            case 0:

                int hrID0 = (int) params[1];

                new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", hrID0) {
                    @Override
                    protected void onSuccess(RealmList<UserDrugs> realmList) {
                        if (realmList != null) {
                            view.onGetListDrug(realmList);
                        }
                    }
                };

                break;
            case 1:

                RESP_Schedule_Drug respScheduleDrug = (RESP_Schedule_Drug) params[1];

                new AddListScheduleModel(JsonHelper.toJson(respScheduleDrug)) {
                    @Override
                    protected void onSuccess(List<ScheduleDrug> scheduleDrugs) {
                        iCmd(2, scheduleDrugs.get(0));
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 2:

                final ScheduleDrug scheduleDrug3 = (ScheduleDrug) params[1];

                new SaveScheduleModel(scheduleDrug3) {
                    @Override
                    protected void onSuccess() {
                        ReminderUtils.startService(view.getActivity(), scheduleDrug3.getId());
                        view.onAddScheduleSuccess(scheduleDrug3);
                    }

                    @Override
                    protected void onError() {
                        ReminderUtils.startService(view.getActivity(), scheduleDrug3.getId());
                        view.onAddScheduleSuccess(scheduleDrug3);
                    }
                };

                break;
        }
    }

    public void getData(int hrID) {
        iCmd(-1, hrID);
        iCmd(0, hrID);
    }

    public void createSchedule() {
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

        if (view.getScheduleDrug().getScheduleTimePerDays().get(0).getScheduleType() == 1) {
            if (view.getScheduleDrug().getScheduleTimePerDays().size() > 0) {
                for (int i = view.getScheduleDrug().getScheduleTimePerDays().size() - 1; i > 0; i--) {
//                    ScheduleTimePerDay timeNow = view.getScheduleDrug().getScheduleTimePerDays().get(i);
//                    ScheduleTimePerDay timeBefore = view.getScheduleDrug().getScheduleTimePerDays().get((i - 1));

                    if (view.getScheduleDrug().getScheduleTimePerDays().get(i).getStartTime().equals(view.getScheduleDrug().getScheduleTimePerDays().get((i - 1)).getStartTime())) {
                        showError(304);
                        return;
                    }
                }
            }
        } else {
            ScheduleTimePerDay scheduleTimePerDay = view.getScheduleDrug().getScheduleTimePerDays().get(0);

            long time = TimeUtils.convertTimeToLong(scheduleTimePerDay.getEndTime()) - TimeUtils.convertTimeToLong(scheduleTimePerDay.getStartTime());

            if ((time / AlarmManager.INTERVAL_HOUR) < scheduleTimePerDay.getTime()) {
                showError(304);
                return;
            }
        }

        view.showProgressBar();

        RESP_Schedule_Drug respScheduleDrug = new RESP_Schedule_Drug(view.getScheduleDrug());

        iCmd(1, respScheduleDrug);
    }

    public HealthRecords getHealthRecords() {
        return healthRecords;
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}