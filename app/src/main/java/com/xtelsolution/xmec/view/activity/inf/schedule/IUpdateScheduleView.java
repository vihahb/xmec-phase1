package com.xtelsolution.xmec.view.activity.inf.schedule;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public interface IUpdateScheduleView extends IBasicView {

    void onGetScheduleSuccess(ScheduleDrug scheduleDrug);
    void onGetListDrugSuccess(RealmList<UserDrugs> realmList);

    void onDeleteDrugUserSuccess(UserDrugSchedule userDrugSchedule);
    void onGetDrugScheduleSuccess(UserDrugSchedule userDrugSchedule, int type);

    void onUpdateScheduleSuccess();

    void showProgressBar(int type);

    ScheduleDrug getScheduleDrug();
    RealmList<ScheduleTimePerDay> getTimePerDay();
    Activity getActivity();
}
