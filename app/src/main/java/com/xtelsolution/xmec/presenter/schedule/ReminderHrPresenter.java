package com.xtelsolution.xmec.presenter.schedule;

import android.app.AlarmManager;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.resp.RESP_Update_Schedule;
import com.xtelsolution.xmec.model.server.DeleteScheduleModel;
import com.xtelsolution.xmec.model.server.UpdateScheduleModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.fragment.inf.schedule.IReminderHrView;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/7/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderHrPresenter extends BasicPresenter {
    private IReminderHrView view;

    public ReminderHrPresenter(IReminderHrView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                final ScheduleDrug scheduleDrug1 = (ScheduleDrug) params[1];

                new UpdateScheduleModel(JsonHelper.toJson(scheduleDrug1)) {
                    @Override
                    protected void onSuccess(RESP_Update_Schedule obj) {

                        new SaveObjectModel<ScheduleDrug>(scheduleDrug1) {
                            @Override
                            protected void onSuccess() {
                                ReminderUtils.startReminderSchedule(view.getFragment().getContext(), scheduleDrug1, -1);
                                view.onUpdateSuccess(scheduleDrug1);
                            }

                            @Override
                            protected void onError() {
                                view.onUpdateSuccess(scheduleDrug1);
                            }
                        };
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
            case 2:

                final ScheduleDrug scheduleDrug2 = (ScheduleDrug) params[1];
                final String oldTime = (String) params[2];
                final int childPos = (int) params[3];

                new UpdateScheduleModel(JsonHelper.toJson(scheduleDrug2)) {
                    @Override
                    protected void onSuccess(RESP_Update_Schedule obj) {

                        new SaveObjectModel<ScheduleDrug>(scheduleDrug2) {
                            @Override
                            protected void onSuccess() {
                                ReminderUtils.startReminderSchedule(view.getFragment().getContext(), scheduleDrug2, -1);
                                view.onUpdateTimeSuccess(scheduleDrug2);
                            }

                            @Override
                            protected void onError() {
                                view.onUpdateTimeSuccess(scheduleDrug2);
                            }
                        };
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION) {
                            showError(errorCode);
                        } else {

                            if (scheduleDrug2.getScheduleTimePerDays().get(0).getScheduleType() == 1) {
                                scheduleDrug2.getScheduleTimePerDays().get(childPos).setStartTime(oldTime);
                            } else {
                                if (childPos == 0) {
                                    scheduleDrug2.getScheduleTimePerDays().get(0).setStartTime(oldTime);
                                } else {
                                    scheduleDrug2.getScheduleTimePerDays().get(0).setEndTime(oldTime);
                                }
                            }

                            view.onUpdateTimeError(scheduleDrug2);
                        }
                    }
                };

                break;
            case 3:

                int id3 = (int) params[1];
                final int type3 = (Integer) params[2];

                new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", id3) {
                    @Override
                    protected void onSuccess(ScheduleDrug object) {
                        if (object != null)
                            view.onGetUserSchedule(object, type3);
                    }
                };

                break;
            case 4:

                final int position4 = (int) params[1];
                final int id4 = (int) params[2];

                new DeleteScheduleModel(id4) {
                    @Override
                    protected void onSuccess() {
                        ReminderUtils.cancelReminderSchedule(view.getFragment().getContext(), id4);
                        iCmd(5, position4, id4);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
            case 5:

                final int position5 = (int) params[1];
                int id5 = (int) params[2];

                new DeleteObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", id5) {
                    @Override
                    protected void onSuccess() {
                        view.onDeleteSuccess(position5);
                    }

                    @Override
                    protected void onError() {
                        view.onDeleteSuccess(position5);
                    }
                };

                break;
        }
    }

    public void changeAlarm(ScheduleDrug scheduleDrug) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        int state = scheduleDrug.getState();
        state = state == 1 ? 2 : 1;
        scheduleDrug.setState(state);

        view.showProgressBar(state);
        iCmd(1, scheduleDrug);
    }

    public void changeTimeAlarm(ScheduleDrug scheduleDrug, String oldTime, int childPos, String timeSet) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        if (scheduleDrug.getScheduleTimePerDays().get(0).getScheduleType() == 1) {
            scheduleDrug.getScheduleTimePerDays().get(childPos).setStartTime(timeSet);
        } else {
            if (childPos == 0) {
                scheduleDrug.getScheduleTimePerDays().get(0).setStartTime(timeSet);
            } else {
                scheduleDrug.getScheduleTimePerDays().get(0).setEndTime(timeSet);
            }
        }

        if (scheduleDrug.getScheduleTimePerDays().get(0).getScheduleType() == 1) {
            if (scheduleDrug.getScheduleTimePerDays().size() > 0) {
                for (int i = scheduleDrug.getScheduleTimePerDays().size() - 1; i > 0; i--) {
//                    ScheduleTimePerDay timeNow = scheduleDrug.getScheduleTimePerDays().get(i);
//                    ScheduleTimePerDay timeBefore = scheduleDrug.getScheduleTimePerDays().get((i - 1));

                    if (scheduleDrug.getScheduleTimePerDays().get(i).getStartTime().equals(scheduleDrug.getScheduleTimePerDays().get((i - 1)).getStartTime())) {
                        scheduleDrug.getScheduleTimePerDays().get(childPos).setStartTime(oldTime);
                        showError(300);
                        return;
                    }
                }
            }
        } else {
            ScheduleTimePerDay scheduleTimePerDay = scheduleDrug.getScheduleTimePerDays().get(0);

            long time = TimeUtils.convertTimeToLong(scheduleTimePerDay.getEndTime()) - TimeUtils.convertTimeToLong(scheduleTimePerDay.getStartTime());

            if ((time / AlarmManager.INTERVAL_HOUR) < scheduleTimePerDay.getTime()) {
                if (childPos == 0) {
                    scheduleDrug.getScheduleTimePerDays().get(0).setStartTime(oldTime);
                } else {
                    scheduleDrug.getScheduleTimePerDays().get(0).setEndTime(oldTime);
                }

                showError(300);
                return;
            }
        }

        view.showProgressBar(3);
        iCmd(2, scheduleDrug, oldTime, childPos);
    }

    public void getUserSchedule(int id, int type) {
        iCmd(3, id, type);
    }

    public void deleteSchedule(int position, int id) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext().getApplicationContext())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(4);
        iCmd(4, position, id);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}