package com.xtelsolution.xmec.view.activity.inf.schedule;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public interface ICreateScheduleView extends IBasicView {

    void onGetDataSuccess(boolean isSuccess);
    void onGetListDrug(RealmList<UserDrugs> realmList);
    void onAddScheduleSuccess(ScheduleDrug scheduleDrug);
    void showProgressBar();

    ScheduleDrug getScheduleDrug();
    Activity getActivity();
}
