package com.xtelsolution.xmec.view.activity.inf.schedule;

import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/15/2017
 * Email: leconglongvu@gmail.com
 */
public interface IUpdateDrugScheduleView extends IBasicView {

    void showProgressBar();

    void onGetDrugSuccess(UserDrugSchedule userDrugSchedule);
    void onGetListDrugSuccess(RealmList<UserDrugs> realmList);
    void onUpdateSuccess();

    UserDrugSchedule getDrugSchedule();
}