package com.xtelsolution.xmec.view.activity.inf.hr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/27/2017
 * Email: leconglongvu@gmail.com
 */
public interface IAddDrugView extends IBasicView {

    void onDeleteSuccess(UserDrugs userDrugs);
    void onGetDrugScheduleSuccess(UserDrugs userDrugs, int type);

    void onAddSuccess();
    void showProgressBar(int type);

//    HealthRecords getHealthRecord();
    List<UserDrugs> getListUserDrugs();
    Activity getActivity();
}